package ru.skypro.avito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.avito.model.Image;
import ru.skypro.avito.repository.ImageRepository;
import ru.skypro.avito.service.impl.ImageServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    /**
     * Данный тест testRemove() представляет собой юнит-тест для метода remove() сервиса imageService, который выполняет удаление изображения.
     */
    @Test
    public void testRemove() {

        // Создается объект Image с идентификатором (id) равным 1 и данными (data) в виде массива байт, содержащих строку "image".
        Image image = new Image();
        image.setId(1);
        image.setData("image".getBytes());

        // С помощью метода doNothing().when(imageRepository).delete(image) настраивается заглушка (mock) на вызов метода delete()
        // репозитория imageRepository с переданным объектом Image в качестве аргумента.
        // Заглушка говорит, что при вызове метода delete() с объектом image ничего не должно происходить.
        doNothing().when(imageRepository).delete(image);

        // Вызывается метод imageService.remove(image) для удаления изображения с использованием сервиса imageService.
        imageService.remove(image);

        // С помощью метода verify(imageRepository, only()).delete(image) проверяется, что метод delete() репозитория imageRepository
        // был вызван ровно один раз с переданным объектом Image в качестве аргумента.
        // Метод only() указывает, что должен быть вызван только указанный метод delete() и не должно быть других вызовов на репозитории imageRepository.
        verify(imageRepository, only()).delete(image);

        // С помощью метода verifyNoMoreInteractions(imageRepository) проверяется,
        // что после вызова метода delete() на репозитории imageRepository больше нет других вызовов методов.
        verifyNoMoreInteractions(imageRepository);
    }
}
