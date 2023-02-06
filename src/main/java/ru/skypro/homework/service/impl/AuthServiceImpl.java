package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.UserHasNoRightsException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;
    private final UserService userService;

    private final UserRepository userRepository;


    public AuthServiceImpl(UserDetailsManager manager, UserService userService, UserRepository userRepository) {
        this.manager = manager;
        this.userService = userService;
        this.encoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    /**
     * Check if user can log in
     *
     * @param userName username from a client
     * @param password password from a client
     * @return boolean result of login
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
     * New user registration
     *
     * @param registerReqDto new user credentials from a client
     * @param role user role
     * @return boolean result of registration
     */
    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {
        if (manager.userExists(registerReqDto.getUsername())) {
            return false;
        }
        //create new User with UserDetailsManager and save it in DB
        manager.createUser(
                org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                        .password(registerReqDto.getPassword())
                        .username(registerReqDto.getUsername())
                        .roles(role.name())
                        .build()
        );
        // Get newly created user from DB and assign other fields
        User savedUser = userService.getUser(registerReqDto.getUsername());
        savedUser.setFirstName(registerReqDto.getFirstName());
        savedUser.setLastName(registerReqDto.getLastName());
        savedUser.setPhone(registerReqDto.getPhone());
        savedUser.setRegDate(LocalDateTime.now());

        // Update newly created User in DB
        userRepository.save(savedUser);
        return true;
    }

    /***
     * Change password for user
     *
     * @param passwordDto dto with old and new passwords from a client
     * @param authentication authentication instance from controller
     */
    @Override
    public void changePassword(NewPasswordDto passwordDto, Authentication authentication) {
        UserDetails userDetails = manager.loadUserByUsername(authentication.getName());
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        if (encoder.matches(passwordDto.getCurrentPassword(), encryptedPasswordWithoutEncryptionType)) {
            User user = userService.getUser(authentication.getName());
            user.setPassword("{bcrypt}" + encoder.encode(passwordDto.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new UserHasNoRightsException("User inputs wrong current password");
        }
    }
}
