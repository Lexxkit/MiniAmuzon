package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
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

    @Override
    public ResponseWrapperUserDto getAllUsers() {
        log.info("Was invoked findAllUsers method from ");
        List<User> userList = userRepository.findAll();
        return userMapper.userListToResponseWrapperUserDto(userList.size(), userList);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findUserByEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        User response = userRepository.save(user);

        return userMapper.userToUserDto(response);
    }

    @Override
    public void updateUserImage(String username, MultipartFile file) {

        User testUser = new User(); // TODO: 24.01.2023 refactor with real user from DB after authorization task!!!

        Avatar avatar = avatarRepository.findByUserId(testUser.getId()).orElse(new Avatar());

        try {
            avatar.setImage(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Problems with uploaded image");
        }
        avatar.setUser(testUser);

        avatarRepository.save(avatar);
    }


}
