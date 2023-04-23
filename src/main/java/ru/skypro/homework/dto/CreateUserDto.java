package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс DTO используется для передачи данных для создания нового пользователя
 */

@Data
public class CreateUserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
}
