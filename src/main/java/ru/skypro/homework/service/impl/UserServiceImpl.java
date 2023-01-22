package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

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
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findUserByEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        User response = userRepository.save(user);

        return userMapper.userToUserDto(response);
    }
}
