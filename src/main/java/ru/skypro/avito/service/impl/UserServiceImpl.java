package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.avito.dto.NewPassword;
import ru.skypro.avito.dto.UserDto;
import ru.skypro.avito.exception.BadCredentialsException;
import ru.skypro.avito.exception.IncorrectArgumentException;
import ru.skypro.avito.exception.UsernameNotFoundException;
import ru.skypro.avito.mapper.UserMapper;
import ru.skypro.avito.model.User;
import ru.skypro.avito.repository.UserRepository;
import ru.skypro.avito.service.UserService;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AvatarServiceImpl avatarService;

    /**
     * Метод updatePassword() предназначен для обновления пароля пользователя в системе.
     * @param newPassword
     * @param authentication
     */
    @Override
    public void updatePassword(NewPassword newPassword, Authentication authentication) {

        // Получается объект User из базы данных на основе имени пользователя (authentication.getName()),
        // вызывая метод getUserByUsername() с использованием репозитория userRepository.
        User user = getUserByUsername(authentication.getName());

        // Проверяется соответствие текущего пароля пользователя, указанного в объекте newPassword, с его текущим хэшированным паролем в базе данных,
        // с использованием метода passwordEncoder.matches().
        // Если пароли не совпадают, выбрасывается исключение BadCredentialsException, указывающее на неправильный текущий пароль.
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException();
        }

        // Если текущий пароль совпадает, то пароль пользователя обновляется с использованием метода passwordEncoder.encode(),
        // который хэширует новый пароль.
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));

        // Обновленный объект User сохраняется в базе данных с использованием метода userRepository.save().
        userRepository.save(user);

        // Выводится отладочное сообщение в лог о успешном обновлении пароля для пользователя с указанным именем.
        log.debug("Password updated for user: {}", authentication.getName());
    }

    /**
     * Метод getUser() предназначен для получения деталей пользователя в системе на основе аутентификации.
     * @param authentication
     * @return
     */
    @Override
    public UserDto getUser(Authentication authentication) {

        // Получается имя пользователя из объекта authentication с использованием метода authentication.getName(),
        // которое представляет имя аутентифицированного пользователя.
        log.info("Returning details for user: {}", authentication.getName());

        // Вызывается метод getUserByUsername() с использованием репозитория userRepository для получения объекта User из базы данных
        // на основе имени пользователя.
        // Объект User преобразуется в UserDto с использованием маппера UserMapper.INSTANCE.toDto().
        // Выводится информационное сообщение в лог о возвращении деталей пользователя с указанным именем.
        return UserMapper.INSTANCE.toDto(getUserByUsername(authentication.getName()));

        // Возвращается объект UserDto с деталями пользователя.
    }

    /**
     * Метод updateUser() предназначен для обновления деталей пользователя в системе на основе переданных данных в объекте UserDto,
     * и аутентификации текущего пользователя.
     * @param userDto
     * @param authentication
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto, Authentication authentication) {

        // Проверяется, что переданные данные userDto содержат необходимую информацию, такую как firstName, lastName и phone.
        // Если какое-либо из этих полей отсутствует или пустое, выбрасывается исключение IncorrectArgumentException.
        if(userDto.getFirstName() == null || userDto.getFirstName().isBlank()
            || userDto.getLastName() == null || userDto.getLastName().isBlank()
            || userDto.getPhone() == null || userDto.getPhone().isBlank()) throw new IncorrectArgumentException();

        // Вызывается метод getUserByUsername() с использованием репозитория userRepository для получения объекта User из базы данных
        // на основе имени пользователя из объекта authentication.
        // Обновляются поля firstName, lastName и phone объекта User на основе значений из объекта userDto.
        User user = getUserByUsername(authentication.getName());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());

        // Сохраняется объект User в базе данных с использованием репозитория userRepository.
        userRepository.save(user);

        // Выводится информационное сообщение в лог о обновлении деталей пользователя с указанным именем.
        log.debug("User details updated for user: {}", authentication.getName());

        // Обновленный объект User преобразуется в UserDto с использованием маппера UserMapper.INSTANCE.toDto().
        // Возвращается объект UserDto с обновленными деталями пользователя.
        return UserMapper.INSTANCE.toDto(user);
    }

    /**
     * Метод updateUserAvatar() предназначен для обновления аватара пользователя на основе переданного файла изображения (MultipartFile),
     * и аутентификации текущего пользователя.
     * @param avatar
     * @param authentication
     * @throws IOException
     */
    @Override
    public void updateUserAvatar(MultipartFile avatar,
                                 Authentication authentication) throws IOException {

        // Вызывается метод getUserByUsername() с использованием репозитория userRepository для получения объекта User
        // из базы данных на основе имени пользователя из объекта authentication.
        User user = getUserByUsername(authentication.getName());

        // Проверяется, существует ли у пользователя уже аватар (user.getAvatar() != null).
        // Если аватар уже существует, то вызывается метод remove() сервиса avatarService для удаления старого аватара пользователя.
        if (user.getAvatar() != null) {
            avatarService.remove(user.getAvatar());
        }

        // Вызывается метод uploadImage() сервиса avatarService для загрузки нового файла изображения аватара пользователя.
        // Результат сохраняется в поле avatar объекта User.
        user.setAvatar(avatarService.uploadImage(avatar));

        // Сохраняется объект User в базе данных с использованием репозитория userRepository.
        userRepository.save(user);

        // Выводится информационное сообщение в лог о успешном обновлении аватара пользователя с указанным именем.
        log.debug("Avatar updated for user: {}", authentication.getName());
    }

    /**
     * Данный метод getUserByUsername() предназначен для получения объекта User из базы данных на основе имени пользователя (username),
     * с использованием репозитория userRepository.
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {

        // Вызывается метод findByUsernameIgnoreCase() репозитория userRepository с переданным именем пользователя для выполнения поиска в базе данных.
        // Метод findByUsernameIgnoreCase() выполняет поиск записи пользователя по имени пользователя, игнорируя регистр букв.
        // Если запись пользователя с указанным именем не найдена (Optional объект пуст), то вызывается исключение UsernameNotFoundException,
        // которое указывает, что пользователь с указанным именем не найден.
        // Если запись пользователя с указанным именем найдена, то объект User возвращается как результат выполнения метода.
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(UsernameNotFoundException::new);
    }
}

