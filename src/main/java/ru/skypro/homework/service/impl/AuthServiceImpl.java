package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public AuthServiceImpl(UserDetailsManager manager, UserRepository userRepository, UserMapper userMapper) {
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
        //create new User
        User user = new User();
        //set Fields from Dto to user
        user.setEmail(registerReqDto.getUsername());
        user.setFirstName(registerReqDto.getFirstName());
        //save user in DB
//        manager.createUser(
//
//                User.withDefaultPasswordEncoder()
//                        .password(registerReqDto.getPassword())
//                        .username(registerReqDto.getUsername())
//                        .roles(role.name())
//                        .build()
//        );
        return true;
    }
}
