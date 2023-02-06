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
     * Receive all users from the DB.
     * The repository method {@link UserRepository#findAll()} is used.
     *
     * @return {@link ResponseWrapperUserDto} with the number of users and list of {@link UserDto}
     */
    @Override
    public ResponseWrapperUserDto getAllUsers() {
        log.info("Was invoked findAllUsers method from ");
        List<User> userList = userRepository.findAll();
        return userMapper.userListToResponseWrapperUserDto(userList.size(), userList);
    }

    /**
     * Update current user with new information.
     *
     * @param userDto dto from a client with new information
     * @param username name to find a user in the DB
     * @return {@link UserDto} with updated information
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

    /**
     * Update user's avatar or create a new one from a file.
     *
     * @param username name to find a user in the DB
     * @param file {@link MultipartFile} with the image to save
     */
    @Override
    public void updateUserAvatar(String username, MultipartFile file) {
        log.info("Was invoked createImage method from {}", UserService.class.getSimpleName());
        if (file.isEmpty()) {
            log.warn("File '{}' is empty.", file.getOriginalFilename());
            throw new EmptyFileException();
        }

        User currentUser = getUser(username);

        Avatar avatar = avatarRepository.findByUserId(currentUser.getId()).orElse(new Avatar());

        try {
            avatar.setImage(file.getBytes());
        } catch (IOException e) {
            log.error("File '{}' has some problems and cannot be read.", file.getOriginalFilename());
            throw new RuntimeException("Problems with uploaded image");
        }
        avatar.setUser(currentUser);

        avatarRepository.save(avatar);
    }

    /**
     * Return a {@link User} by its username from the DB.
     * @param username name to find a user in the DB
     * @return {@link User} entity
     * @throws UserNotFoundException if user was not found
     */
    @Override
    public User getUser(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {
        User response = getUser(username);
        return userMapper.userToUserDto(response);
    }

    /**
     * Check if username from {@link Authentication#getName()} and username equals
     * OR if user has role ADMIN. Throws exception if none of the conditions are true.
     * @param authentication {@link Authentication} instance from controller
     * @param username name of a user
     * @throws UserHasNoRightsException
     */
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
