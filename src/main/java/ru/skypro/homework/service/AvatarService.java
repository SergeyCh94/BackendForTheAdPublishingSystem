package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    Avatar uploadAvatar(Long userId, MultipartFile file) throws IOException;
    Avatar getAvatarById (Long avatarId);
}
