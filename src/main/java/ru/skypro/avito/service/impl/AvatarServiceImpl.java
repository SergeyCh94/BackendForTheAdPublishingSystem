package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.avito.exception.ImageNotFoundException;
import ru.skypro.avito.model.Avatar;
import ru.skypro.avito.repository.AvatarRepository;
import ru.skypro.avito.service.ImageService;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AvatarServiceImpl implements ImageService<Avatar> {

    private final AvatarRepository avatarRepository;

    /**
     * Метод remove() предполагает удаление объекта Avatar из репозитория на основе переданного объекта avatar.
     * @param avatar
     */
    @Override
    public void remove(Avatar avatar) {
        // Выводится отладочное сообщение в лог о начале удаления аватара, используя идентификатор аватара из объекта avatar.
        log.debug("Removing avatar with id {}", avatar.getId());

        // Используется репозиторий avatarRepository для удаления объекта avatar из базы данных, используя метод delete(avatar).
        avatarRepository.delete(avatar);
        log.info("Avatar removed successfully");
    }

    /**
     * Метод uploadImage() предполагает загрузку файла аватара в базу данных.
     * @param avatarFile
     * @return
     * @throws IOException
     */
    @Override
    public Avatar uploadImage(MultipartFile avatarFile) throws IOException {
        // Выводится отладочное сообщение в лог о начале загрузки файла аватара, используя оригинальное имя файла из объекта avatarFile.
        log.debug("Uploading avatar file: {}", avatarFile.getOriginalFilename());

        // Используется репозиторий avatarRepository для сохранения объекта avatar в базу данных, используя метод save(avatar).
        Avatar avatar = new Avatar();
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setData(avatarFile.getBytes());
        Avatar savedAvatar = avatarRepository.save(avatar);

        // Выводится информационное сообщение в лог о успешной загрузке аватара с указанием его идентификатора.
        log.info("Avatar successfully uploaded with id {}", savedAvatar.getId());

        // Возвращается сохраненный объект Avatar с присвоенным идентификатором.
        return savedAvatar;
    }

    /**
     * Метод getImageById() предполагает получение объекта аватара по его идентификатору.
     * @param id
     * @return
     */
    @Override
    public Avatar getImageById(Integer id) {
        // Выводится отладочное сообщение в лог о получении аватара по указанному идентификатору.
        log.debug("Getting avatar with id: {}", id);

        // Используется репозиторий avatarRepository для поиска аватара в базе данных по его идентификатору с использованием метода findById(id).
        // Если найденный объект Avatar не пустой, то он возвращается из метода.
        // Если объект Avatar не найден в базе данных, то выбрасывается исключение ImageNotFoundException.
        return avatarRepository.findById(id).orElseThrow(ImageNotFoundException::new);
    }

}
