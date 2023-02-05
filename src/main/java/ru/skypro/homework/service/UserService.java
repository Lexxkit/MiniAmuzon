package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {

    ResponseWrapperUserDto getAllUsers();

    UserDto updateUser(UserDto userdto, String name);

    void updateUserAvatar(String username, MultipartFile file);

    UserDto getUserByEmail(String email);

    void checkIfUserHasPermissionToAlter(Authentication authentication, String username);
}
