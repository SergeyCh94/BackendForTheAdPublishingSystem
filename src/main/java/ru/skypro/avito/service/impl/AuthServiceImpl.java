package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.avito.dto.RegisterReq;
import ru.skypro.avito.exception.BadCredentialsException;
import ru.skypro.avito.exception.IncorrectArgumentException;
import ru.skypro.avito.service.AuthService;
import ru.skypro.avito.service.CustomUserDetailsService;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsService manager;
    private final PasswordEncoder encoder;

    /**
     * Метод login() предполагает аутентификацию пользователя на основе переданных имени пользователя (userName) и пароля (password).
     * @param userName
     * @param password
     * @return
     */
    @Override
    public boolean login(String userName, String password) {
        // Выводится отладочное сообщение в лог, используя уровень логирования "debug",
        // с указанием имени пользователя, который выполняет попытку входа.
        log.debug("Logging in user: {}", userName);

        // Используется объект manager - это объект UserDetails, для загрузки деталей пользователя на основе его имени пользователя,
        // используя метод loadUserByUsername(userName). Это позволяет получить информацию о пользователе, включая его хэшированный пароль.
        UserDetails userDetails = manager.loadUserByUsername(userName);

        // Если userDetails равен null, то выбрасывается исключение AuthenticationException,
        // чтобы указать, что процесс входа завершился неудачно.
        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }

        // Используется объект encoder - это объект PasswordEncoder, для сравнения хэша пароля, переданного в метод, с хэшем пароля,
        // полученным из деталей пользователя (userDetails.getPassword()). Если хэши совпадают, то метод возвращает true, иначе - false.
        boolean passwordMatch = encoder.matches(password, userDetails.getPassword());

        // Если пароль не совпадает, то выбрасывается исключение AuthenticationException,
        // чтобы указать, что процесс входа завершился неудачно.
        if (!passwordMatch) {
            throw new BadCredentialsException("Invalid password");
        }

        // Возвращается результат сравнения хэшей паролей (true, если совпадают, иначе - false).
        return passwordMatch;
    }

    /**
     * Метод register() предполагает регистрацию нового пользователя на основе переданных данных из объекта registerReq.
     * @param registerReq
     * @return
     */
    @Override
    public boolean register(RegisterReq registerReq) {
        // Проверяется, что все обязательные поля в объекте registerReq не равны null и не являются пустыми (isBlank()).
        // Если хотя бы одно из полей не прошло проверку, то выбрасывается исключение IncorrectArgumentException.
        if(registerReq.getUsername() == null || registerReq.getUsername().isBlank()
            || registerReq.getFirstName() == null || registerReq.getFirstName().isBlank()
            || registerReq.getLastName() == null || registerReq.getLastName().isBlank()
            || registerReq.getPhone() == null || registerReq.getPhone().isBlank()
            || registerReq.getPassword() == null || registerReq.getPassword().isBlank()) throw new IncorrectArgumentException();

        // Выводится информационное сообщение в лог о начале регистрации нового пользователя,
        // используя имя пользователя из объекта registerReq.
        log.info("Registering new user: {}", registerReq.getUsername());

        // Используется объект manager - это объект UserDetails, для создания нового пользователя на основе данных из объекта registerReq,
        // используя метод createUser(registerReq).
        manager.createUser(registerReq);

        // Выводится информационное сообщение в лог о успешной регистрации нового пользователя, используя имя пользователя из объекта registerReq.
        log.info("User {} registered successfully", registerReq.getUsername());

        // Метод возвращает логическое значение true, указывая на успешную регистрацию нового пользователя.
        return true;
    }
}
