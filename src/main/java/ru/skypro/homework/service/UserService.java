package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Users;

public interface UserService {
    ResponseEntity<UserDto> getUser(Long id);
    Users findByUsername(String username);
    ResponseEntity<ResponseWrapperUserDto> getUsers();
    ResponseEntity<UserDto> updateUser(UserDto userDto, Authentication authentication);
    ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto newPasswordDto);
    void setAdminInUsers();
}
