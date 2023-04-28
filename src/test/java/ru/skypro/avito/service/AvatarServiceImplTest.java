package ru.skypro.avito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.avito.model.Avatar;
import ru.skypro.avito.repository.AvatarRepository;
import ru.skypro.avito.service.impl.AvatarServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceImplTest {

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private AvatarServiceImpl avatarService;

    /**
     * Данный тест testRemove() представляет собой юнит-тест для метода remove() сервиса avatarService, который выполняет удаление аватара пользователя.
     */
    @Test
    public void testRemove() {

        // Создается объект Avatar с идентификатором (id) равным 1 и данными (data) в виде массива байт, содержащих строку "avatar".
        Avatar avatar = new Avatar();
        avatar.setId(1);
        avatar.setData("avatar".getBytes());

        // С помощью метода doNothing().when(avatarRepository).delete(avatar) настраивается заглушка (mock)
        // на вызов метода delete() репозитория avatarRepository с переданным объектом Avatar в качестве аргумента.
        // Заглушка говорит, что при вызове метода delete() с объектом avatar ничего не должно происходить.
        doNothing().when(avatarRepository).delete(avatar);

        // Вызывается метод avatarService.remove(avatar) для удаления аватара с использованием сервиса avatarService.
        avatarService.remove(avatar);

        // С помощью метода verify(avatarRepository, only()).delete(avatar) проверяется,
        // что метод delete() репозитория avatarRepository был вызван ровно один раз с переданным объектом Avatar в качестве аргумента.
        // Метод only() указывает, что должен быть вызван только указанный метод delete() и не должно быть других вызовов на репозитории avatarRepository.
        verify(avatarRepository, only()).delete(avatar);

        // С помощью метода verifyNoMoreInteractions(avatarRepository) проверяется,
        // что после вызова метода delete() на репозитории avatarRepository больше нет других вызовов методов.
        verifyNoMoreInteractions(avatarRepository);
    }
}

