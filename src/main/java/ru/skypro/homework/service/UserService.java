package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {

    ResponseWrapperUserDto getAllUsers();

    UserDto updateUser(UserDto userDto);

    void updateUserImage(String username, MultipartFile file);
}
