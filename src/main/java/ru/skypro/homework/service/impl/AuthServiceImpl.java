package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsManager manager) {
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Authorization of user
     *
     * @param userName
     * @param password
     * @return PasswordEncoder
     */
    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    /**
     * Of user information
     *
     * @param registerReqDto
     * @param role
     * @return  authenticity of user information
     */
    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {
        if (manager.userExists(registerReqDto.getUsername())) {
            return false;
        }
        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReqDto.getPassword())
                        .username(registerReqDto.getUsername())
                        .roles(role.name())
                        .build()
        );
        return true;
    }
}
