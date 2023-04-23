package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginReqDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.service.AuthService;

import static ru.skypro.homework.enums.Role.USER;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 */

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Метод login() - это метод контроллера, обрабатывающий POST-запрос на URL "/login" и осуществляющий аутентификацию пользователя.
     * @param req
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto req) {
        if (authService.login(req.getUsername(), req.getPassword())) {
            return ResponseEntity.ok().body("Аутентификация прошла успешно!");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Метод register() - это метод контроллера, обрабатывающий POST-запрос на URL "/register" и выполняющий регистрацию нового пользователя
     * @param req
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReqDto req) {
        LOGGER.info("Was invoked method of AuthController for register new user.");
        if (req.getRole() == null) {
            req.setRole(USER);
        }
        if (authService.register(req)) {
            return ResponseEntity.ok().body("Регистрация прошла успешно!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
