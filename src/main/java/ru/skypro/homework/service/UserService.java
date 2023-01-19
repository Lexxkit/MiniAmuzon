package ru.skypro.homework.service;

import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {

    ResponseWrapperUserDto getAllUsers();

    UserDto updateUser(UserDto userDto);
}
