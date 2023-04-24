package ru.skypro.avito.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.avito.dto.NewPassword;
import ru.skypro.avito.dto.UserDto;

import java.io.IOException;

public interface UserService {

    void updatePassword(NewPassword newPassword, Authentication authentication);

    UserDto getUser(Authentication authentication);

    UserDto updateUser(UserDto userDto, Authentication authentication);

    void updateUserAvatar(MultipartFile avatar, Authentication authentication) throws IOException;

}
