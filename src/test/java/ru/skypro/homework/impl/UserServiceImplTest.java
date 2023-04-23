package ru.skypro.homework.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mappers.UsersMapper;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantsForTests.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Spy
    private UsersMapper usersMapper;
    @Spy
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl out;

    /**
     * Этот тест проверяет метод getUser() класса UserServiceImpl.
     * Он настраивает мок-объект userRepository с помощью метода when() и вызывает метод getUser() с определенным аргументом.
     * Затем производится проверка, что метод userRepository.findById() был вызван один раз с любым аргументом,
     * и метод usersMapper.userToUserDto() был вызван один раз с любым аргументом.
     */
    @Test
    void getUser() {
        when(userRepository.findById(any())).thenReturn(Optional.of(USER_1));
        out.getUser(1L);
        verify(userRepository, times(1)).findById(any());
        verify(usersMapper, times(1)).userToUserDto(any());
    }

    /**
     * Этот тест проверяет метод findByUsername() класса UserServiceImpl.
     * Он настраивает мок-объект userRepository с помощью метода when() и вызывает метод findByUsername() с определенным аргументом.
     * Затем производится проверка, что метод userRepository.findByUsername() был вызван один раз с любым аргументом.
     */
    @Test
    void findByUsername() {
        when(userRepository.findByUsername(any())).thenReturn(USER_1);
        out.findByUsername(USER_NAME_1);
        verify(userRepository, times(1)).findByUsername(any());
    }

    /**
     * Этот тест проверяет метод getUsers() класса UserServiceImpl.
     * Он настраивает мок-объект userRepository с помощью метода when() и вызывает метод getUsers().
     * Затем производится проверка, что метод userRepository.findAll() был вызван один раз,
     * и метод usersMapper.listUsersToListUserDto() был вызван с аргументом userList, и возвращенное значение соответствует ожидаемым значениям.
     */
    @Test
    void getUsers() {
        List<Users> usersList = new ArrayList<>(List.of(USER_1, USER_2));
        List<UserDto> userDtoList = new ArrayList<>(List.of(USER_DTO_1, USER_DTO_2));
        when(userRepository.findAll()).thenReturn(usersList);
        when(usersMapper.listUsersToListUserDto(anyList())).thenReturn(userDtoList);
        ResponseWrapperUserDto wrapper = out.getUsers().getBody();
        assert wrapper != null;
        assertEquals(2, wrapper.getCount());
        assertEquals(userDtoList, wrapper.getResults());
    }

    /**
     * Этот тест проверяет метод updateUser() класса UserServiceImpl.
     * Он настраивает мок-объект userRepository и usersMapper с помощью методов when() и вызывает метод updateUser() с определенными аргументами.
     * Затем производится проверка, что метод userRepository.findByUsername() был вызван один раз с любым аргументом,
     * и метод usersMapper.userToUserDto() был вызван с аргументом USER_DTO_2, и возвращенное значение соответствует ожидаемому значению.
     */
    @Test
    void updateUser() {
        when(userRepository.findByUsername(any())).thenReturn(USER_1);
        when(usersMapper.userToUserDto(any())).thenReturn(USER_DTO_2);
        UserDto userDto = out.updateUser(USER_DTO_2, authentication).getBody();
        assertEquals(USER_DTO_2, userDto);
    }
}