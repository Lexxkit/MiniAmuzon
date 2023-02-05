package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @InjectMocks
    private UserServiceImpl out;

    private User testUser;

    @BeforeEach
    void init() {
        testUser = new User();
        testUser.setId(42L);
        testUser.setEmail("test@test.com");
    }

    @Test
    void shouldThrowUserNotFoundException_whenGetUserByEmailNotInDB() {
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> out.getUserByEmail("Wrong email"));
    }

    @Test
    void shouldReturnUserDto_whenGetUserByEmail() {
        when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(testUser));
        UserDto result = out.getUserByEmail(testUser.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getLastName()).isNull();
    }
}
