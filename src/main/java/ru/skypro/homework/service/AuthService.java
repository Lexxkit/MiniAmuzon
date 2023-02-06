package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;

public interface AuthService {
    boolean login(String userName, String password);
    boolean register(RegisterReqDto registerReqDto, Role role);

    void changePassword(NewPasswordDto passwordDto, Authentication authentication);
}
