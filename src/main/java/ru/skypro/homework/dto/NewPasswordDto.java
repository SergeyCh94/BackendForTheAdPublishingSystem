package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс DTO используется для передачи данных, связанных с обновлением пароля пользователя
 */

@Data
public class NewPasswordDto {
    private String currentPassword;
    private String newPassword;
}
