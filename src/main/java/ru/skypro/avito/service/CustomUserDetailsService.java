package ru.skypro.avito.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.avito.dto.CustomUserDetails;
import ru.skypro.avito.dto.RegisterReq;
import ru.skypro.avito.enums.Role;
import ru.skypro.avito.exception.IncorrectUsernameException;
import ru.skypro.avito.model.User;
import ru.skypro.avito.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Данный метод loadUserByUsername() является реализацией интерфейса UserDetailsService из Spring Security
     * и предназначен для загрузки деталей пользователя на основе имени пользователя (username), используя репозиторий userRepository
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Вызывается метод findByUsernameIgnoreCase() репозитория userRepository с переданным именем пользователя для выполнения поиска в базе данных.
        // Метод findByUsernameIgnoreCase() выполняет поиск записи пользователя по имени пользователя, игнорируя регистр букв,
        // и возвращает Optional объект, содержащий найденного пользователя или пустой объект, если пользователь не найден.
        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);

        // Если найденный пользователь присутствует (optionalUser.isPresent() возвращает true),
        // то создается объект CustomUserDetails с использованием найденного пользователя (user) в качестве аргумента.
        // CustomUserDetails является пользовательской реализацией интерфейса UserDetails, который предоставляет информацию о пользователе,
        // необходимую для аутентификации и авторизации в Spring Security.
        // Созданный объект CustomUserDetails возвращается в качестве результата выполнения метода.
        // Если пользователь с указанным именем не найден (optionalUser.isPresent() возвращает false),
        // то вызывается исключение UsernameNotFoundException с сообщением "User not found with username: " + username, которое указывает,
        // что пользователь с указанным именем не найден.
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new CustomUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    /**
     * Данный метод createUser() отвечает за создание нового пользователя на основе переданных данных из объекта RegisterReq и сохранение его в базе данных.
     * @param registerReq
     */
    public void createUser(RegisterReq registerReq) {

        // Вызывается метод findByUsernameIgnoreCase() репозитория userRepository с переданным именем пользователя (registerReq.getUsername())
        // для проверки наличия пользователя с таким же именем в базе данных.
        // Если пользователь с указанным именем уже существует (userRepository.findByUsernameIgnoreCase(registerReq.getUsername()).isPresent()
        // возвращает true), то выбрасывается исключение IncorrectUsernameException().
        if (userRepository.findByUsernameIgnoreCase(registerReq.getUsername()).isPresent()) {
            throw new IncorrectUsernameException();
        }

        // Если пользователь с указанным именем не существует (userRepository.findByUsernameIgnoreCase(registerReq.getUsername()).isPresent() возвращает false),
        // то создается новый объект User.
        // Устанавливаются значения полей объекта User на основе переданных данных из объекта RegisterReq,
        // такие как имя пользователя (registerReq.getUsername()), зашифрованный пароль (passwordEncoder.encode(registerReq.getPassword())),
        // роль (Role.USER), имя (registerReq.getFirstName()), фамилия (registerReq.getLastName()), телефон (registerReq.getPhone()) и
        // флаг активности (user.setEnabled(true)).
        User user = new User();
        user.setUsername(registerReq.getUsername());
        user.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        user.setRole(Role.USER);
        user.setFirstName(registerReq.getFirstName());
        user.setLastName(registerReq.getLastName());
        user.setPhone(registerReq.getPhone());
        user.setEnabled(true);

        // Созданный объект User сохраняется в базе данных с использованием метода userRepository.save(user),
        // который выполняет операцию сохранения объекта User в базе данных.
        // Метод успешно завершается без возвращаемого значения.
        userRepository.save(user);
    }
}
