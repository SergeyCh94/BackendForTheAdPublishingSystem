package ru.skypro.avito.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService<T> {

    void remove(T object);

    T uploadImage(MultipartFile file) throws IOException;

    T getImageById(Integer id);

}
