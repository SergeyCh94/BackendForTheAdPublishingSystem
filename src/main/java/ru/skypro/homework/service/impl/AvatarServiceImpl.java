package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;


import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Это класс-сервис AvatarServiceImpl,
 * который реализует интерфейс AvatarService и предоставляет функциональность для загрузки и получения аватарок пользователей.
 */
@Slf4j
@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarServiceImpl.class);

    /**
     * Директорий, где будут храниться файлы с аватарками.
     */
    @Value("/marketPlace/avatars")
    private String avatarDir;
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private static final String END_POINT_FOR_AVATAR = "/getAvatar/";

    public AvatarServiceImpl(AvatarRepository avatarRepository, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    /**
     * Метод uploadAvatar используется для загрузки аватарки пользователя.
     * Он принимает идентификатор пользователя и объект MultipartFile, представляющий файл изображения, в качестве входных данных.
     * Метод создает новую сущность Avatar, устанавливает пользователя для аватарки и сохраняет файл изображения в каталог, указанный в поле avatarDir.
     * Подробности файла, такие как путь к файлу, тип медиа, размер файла и данные файла, сохраняются в сущность Avatar,
     * и поля аватарки и изображения пользователя обновляются в базе данных. Возвращается сохраненная сущность Avatar.
     * @param userId - идентификатор пользователя.
     * @param file - изображение аватара.
     * @return - сохраненный аватар.
     */
    public Avatar uploadAvatar(Long userId, MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method for uploading avatar for user.");
        Avatar avatar = new Avatar();
        Users user = userRepository.findById(userId).orElseThrow();
        avatar.setUsers(user);
        String pathOfAvatar = avatarDir + "/" + userId;
        if (file != null) {
            Path filePath = Path.of(pathOfAvatar, user.getLastName()  + "." +
                    getExtension(Objects.requireNonNull(file.getOriginalFilename())));

            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ){
                bis.transferTo(bos);
            }
            avatar.setFilePath(filePath.toString());
            avatar.setMediaType(Objects.requireNonNull(file.getContentType()));
            avatar.setFileSize(file.getSize());
            avatar.setData(file.getBytes());
            Avatar savedAvatar = avatarRepository.save(avatar);
            user.setAvatar(avatar);
            user.setImage(END_POINT_FOR_AVATAR + savedAvatar.getId());
            userRepository.save(user);
            return savedAvatar;
        }
        return null;
    }

    /**
     * Метод getAvatarById используется для получения аватарки по ее идентификатору.
     * Он принимает идентификатор аватарки в качестве входных данных и возвращает соответствующую сущность Avatar из базы данных.
     * @param avatarId - идентификатор аватара.
     * @return - найденный аватар.
     */
    @Override
    public Avatar getAvatarById(Long avatarId) {
        LOGGER.info("Was invoked method for get avatar by id.");
        return avatarRepository.findById(avatarId).orElseThrow();
    }

    /**
     * Метод getExtension вспомогательный, который извлекает расширение файла из заданного имени файла.
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
