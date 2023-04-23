package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;

import java.io.IOException;

public interface ImageService {
    void uploadImage(Long adsId, MultipartFile file) throws IOException;
    Images getImageById(Long id);
    Images getImagesByAds(Ads ads);
    void removeImagesByAds(Ads ads);
}
