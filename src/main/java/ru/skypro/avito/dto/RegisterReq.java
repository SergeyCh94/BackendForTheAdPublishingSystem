package ru.skypro.avito.dto;

import lombok.Data;
import ru.skypro.avito.enums.Role;

@Data
public class RegisterReq {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
