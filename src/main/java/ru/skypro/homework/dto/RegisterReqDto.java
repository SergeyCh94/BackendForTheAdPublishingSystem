package ru.skypro.homework.dto;

import lombok.Data;
import ru.skypro.homework.enums.Role;

/**
 * Класс DTO используется для передачи данных, связанных с регистрацией нового пользователя
 */

@Data
public class RegisterReqDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
