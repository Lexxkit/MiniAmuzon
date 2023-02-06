package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.EmptyFileException;
import ru.skypro.homework.exceptions.UserHasNoRightsException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Receive all users
     * The repository method is being used{@link UserRepository#findAll()}
     *
     * @return all users
     */
    @Override
    public ResponseWrapperUserDto getAllUsers() {
        log.info("Was invoked findAllUsers method from ");
        List<User> userList = userRepository.findAll();
        return userMapper.userListToResponseWrapperUserDto(userList.size(), userList);
    }

    /**
     * Method for editing a user and saving it to DB
     *
     * @param userDto
     * @param username
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto, String username) {
        User user = getUser(username);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        User response = userRepository.save(user);

        return userMapper.userToUserDto(response);
    }

    @Override
    public void updateUserAvatar(String username, MultipartFile file) {
        log.info("Was invoked createImage method from {}", UserService.class.getSimpleName());
        if (file.isEmpty()) {
            log.warn("File '{}' is empty.", file.getOriginalFilename());
            throw new EmptyFileException();
        }

        User testUser = getUser(username);

        Avatar avatar = avatarRepository.findByUserId(testUser.getId()).orElse(new Avatar());

        try {
            avatar.setImage(file.getBytes());
        } catch (IOException e) {
            log.error("File '{}' has some problems and cannot be read.", file.getOriginalFilename());
            throw new RuntimeException("Problems with uploaded image");
        }
        avatar.setUser(testUser);

        avatarRepository.save(avatar);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {
        User response = getUser(username);
        return userMapper.userToUserDto(response);
    }

    @Override
    public void checkIfUserHasPermissionToAlter(Authentication authentication, String username) {
        boolean matchUser = authentication.getName().equals(username);
        boolean userIsAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().contains(Role.ADMIN.name()));

        if (!(userIsAdmin || matchUser)){
            log.warn("Current user has NO rights to perform this operation.");
            throw new UserHasNoRightsException("Current user has NO rights to perform this operation.");
        }
    }
}
