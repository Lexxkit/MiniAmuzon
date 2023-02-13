package ru.skypro.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.skypro.homework.controller.AdsController;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.controller.ImageController;
import ru.skypro.homework.controller.UserController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeworkApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AdsController adsController;
    @Autowired
    private AuthController authController;
    @Autowired
    private UserController userController;
    @Autowired
    private ImageController imageController;

    @Test
    void contextLoads() {
        assertThat(adsController).isNotNull();
        assertThat(authController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(imageController).isNotNull();
    }

}
