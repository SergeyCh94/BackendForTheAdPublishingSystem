package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс DTO используется для передачи данных, связанных с запросом на аутентификацию или вход в систему
 */

@Data
public class LoginReqDto {
    private String password;
    private String username;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
