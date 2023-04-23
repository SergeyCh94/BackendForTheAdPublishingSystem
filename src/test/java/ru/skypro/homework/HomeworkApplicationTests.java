package ru.skypro.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.skypro.homework.component.BasicAuthCorsFilter;
import ru.skypro.homework.controller.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeworkApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AdsController adsController;
    @Autowired
    private AuthController authController;
    @Autowired
    private BasicAuthCorsFilter basicAuthCorsFilter;
    @Autowired
    private ImageController imageController;
    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        Assertions.assertThat(adsController).isNotNull();
        Assertions.assertThat(authController).isNotNull();
        Assertions.assertThat(basicAuthCorsFilter).isNotNull();
        Assertions.assertThat(imageController).isNotNull();
        Assertions.assertThat(userController).isNotNull();
    }

}
