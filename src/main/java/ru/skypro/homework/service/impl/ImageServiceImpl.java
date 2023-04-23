package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Класс ImageServiceImpl предоставляет функциональность для загрузки, получения и удаления изображений, связанных с объявлениями в приложении.
 */
@Service
@Slf4j
@Transactional
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    /**
     * Директорий, где будут храниться файлы с фотографиями.
     */
    @Value("/marketPlace/images")
    private String imageDir;
    private final ImageRepository imageRepository;
    private final AdsRepository adsRepository;

    public ImageServiceImpl(ImageRepository imageRepository, AdsRepository adsRepository) {
        this.imageRepository = imageRepository;
        this.adsRepository = adsRepository;
    }

    /**
     * Метод uploadImage используется для загрузки файла изображения, связанного с заданным объявлением.
     * Он принимает идентификатор объявления и объект MultipartFile, представляющий загруженный файл, в качестве входных данных.
     * Метод создает новую сущность Images, устанавливает ее свойства, такие как путь к файлу, тип медиа, и размер файла, и сохраняет ее в imageRepository.
     * Затем метод устанавливает загруженное изображение в качестве связанного изображения для объявления и сохраняет объявление в adsRepository.
     * @param adsId
     * @param file
     * @throws IOException
     */
    public void uploadImage(Long adsId, MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method for uploading image for Ads.");
        Images newImage = new Images();
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        newImage.setAds(ads);
        String pathOfAds = imageDir + "/" + adsId;
        if (file != null) {
            Path filePath = Path.of(pathOfAds, ads.getTitle() + "." +
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
            newImage.setFilePath(filePath.toString());
            newImage.setMediaType(file.getContentType());
            newImage.setFileSize(file.getSize());
        }
        imageRepository.save(newImage);
        ads.setImages(newImage);
        adsRepository.save(ads);
    }

    /**
     * Метод getImagesByAdsId извлекает сущность Images, связанную с заданным идентификатором объявления,
     * сначала извлекая объявление из adsRepository, а затем, используя его, находя связанное изображение в imageRepository.
     * @param adsId - идентификатор объявления.
     */
    private Images getImagesByAdsId(Long adsId) {
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        return imageRepository.findByAds(ads);
    }

    /**
     * Метод getExtension вспомогательный, который извлекает расширение файла из заданного имени файла.
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Поиск изображения по его идентификатору
     * @param id - идентификатор изображения.
     */
    public Images getImageById(Long id) {
        LOGGER.info("Was invoked method for get image by Id.");
        return imageRepository.findById(id).orElseThrow();
    }

    public Images getImagesByAds(Ads ads) {
        LOGGER.info("Was invoked method for get images by Ads.");
        return imageRepository.findByAds(ads);
    }

    public void removeImagesByAds(Ads ads) {
        LOGGER.info("Was invoked method for delete images by Ads.");
        imageRepository.deleteById(imageRepository.findByAds(ads).getId());
    }
}
