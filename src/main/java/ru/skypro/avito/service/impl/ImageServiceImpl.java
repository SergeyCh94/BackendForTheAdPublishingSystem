package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.avito.exception.ImageNotFoundException;
import ru.skypro.avito.model.Image;
import ru.skypro.avito.repository.ImageRepository;
import ru.skypro.avito.service.ImageService;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService<Image> {

    private final ImageRepository imageRepository;

    /**
     * Метод remove() предназначен для удаления объекта изображения (Image).
     * @param image
     */
    @Override
    public void remove(Image image) {

        // Вызывается метод delete(image) репозитория imageRepository, который удаляет указанный объект изображения из базы данных.
        imageRepository.delete(image);

        // Выводится информационное сообщение в лог о успешном удалении изображения.
        log.info("Image removed successfully");
    }

    /**
     * Метод uploadImage() предназначен для загрузки изображения (MultipartFile) и сохранения его в базе данных в виде объекта Image.
     * @param imageFile
     * @return
     * @throws IOException
     */
    @Override
    public Image uploadImage(MultipartFile imageFile) throws IOException {

        // Выводится отладочное сообщение в лог о начале загрузки изображения с указанием оригинального имени файла.
        log.debug("Uploading image file: " + imageFile.getOriginalFilename());

        // Создается объект Image и устанавливаются его свойства на основе загруженного файла: тип медиа (mediaType),
        // размер файла (fileSize), и данные файла (data).
        // Вызывается метод save(image) репозитория imageRepository, который сохраняет объект Image в базе данных и возвращает сохраненный объект Image.
        Image image = new Image();
        image.setMediaType(imageFile.getContentType());
        image.setFileSize(imageFile.getSize());
        image.setData(imageFile.getBytes());
        Image savedImage = imageRepository.save(image);

        // Выводится информационное сообщение в лог о успешной загрузке изображения с указанием его идентификатора (id).
        log.info("Image successfully uploaded with id {}", savedImage.getId());

        // Возвращается сохраненный объект Image.
        return savedImage;
    }

    /**
     * Метод getImageById() предназначен для получения объекта Image по его идентификатору (id) из базы данных.
     * @param id
     * @return
     */
    @Override
    public Image getImageById(Integer id) {

        // Выводится отладочное сообщение в лог о попытке получения изображения с указанным идентификатором.
        log.debug("Getting image with id: {}", id);

        // Вызывается метод findById(id) репозитория imageRepository, который пытается найти объект Image в базе данных по его идентификатору (id).
        // Если объект Image с указанным идентификатором найден, то он возвращается из метода.
        // Если объект Image с указанным идентификатором не найден, то выбрасывается исключение ImageNotFoundException.
        // В случае выбрасывания исключения, сообщение об ошибке будет выведено в лог, и исключение будет передано вызывающему коду.
        return imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
    }
}
