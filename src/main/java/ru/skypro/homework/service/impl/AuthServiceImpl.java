package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Класс AuthServiceImpl представляет реализацию сервисного класса,
 * который отвечает за аутентификацию и регистрацию пользователей.
 */

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(UserDetailsManager manager, UserRepository userRepository) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Метод login отвечает за аутентификацию пользователя.
     * Он принимает имя пользователя и пароль в виде строковых параметров.
     * Сначала метод проверяет, существует ли пользователь с таким именем пользователя, используя метод userExists объекта manager.
     * Если пользователь не существует, метод возвращает false.
     * Если пользователь существует, метод загружает объект UserDetails пользователя с помощью метода loadUserByUsername объекта manager.
     * Затем метод извлекает зашифрованный пароль пользователя из объекта UserDetails и сравнивает его с зашифрованным введенным паролем,
     * используя метод matches объекта encoder. Если пароль совпадает, метод возвращает true, иначе - false.
     * @param userName
     * @param password
     * @return
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
     * Метод register отвечает за регистрацию нового пользователя.
     * Он принимает объект RegisterReqDto, содержащий данные нового пользователя.
     * Метод сначала проверяет, существует ли пользователь с таким именем пользователя, используя метод userExists объекта manager.
     * Если пользователь существует, метод возвращает false.
     * Если пользователь не существует, метод создает нового пользователя с помощью метода createUser объекта manager,
     * используя данные из объекта RegisterReqDto.
     * Затем метод извлекает объект Users нового пользователя из базы данных с помощью userRepository,
     * устанавливает необходимые данные нового пользователя и сохраняет его с помощью метода save объекта userRepository.
     * Наконец, метод возвращает true.
     * @param registerReqDto
     * @return
     */
    @Override
    public boolean register(RegisterReqDto registerReqDto) {
        if (manager.userExists(registerReqDto.getUsername())) {
            return false;
        }
        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReqDto.getPassword())
                        .username(registerReqDto.getUsername())
                        .roles(registerReqDto.getRole().name())
                        .build()
        );
        Users newUser = userRepository.findByUsername(registerReqDto.getUsername());
        newUser.setFirstName(registerReqDto.getFirstName());
        newUser.setLastName(registerReqDto.getLastName());
        newUser.setPhone(registerReqDto.getPhone());
        newUser.setRole(registerReqDto.getRole());
        userRepository.save(newUser);
        return true;
    }
}
