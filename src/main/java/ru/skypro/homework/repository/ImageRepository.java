package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;

/**
 * Интерфейс ImageRepository расширяет JpaRepository и предоставляет базовые CRUD-методы для работы с сущностью Images в базе данных
 */
@Repository
public interface ImageRepository extends JpaRepository<Images, Long> {

    /**
     * Метод позволяет искать изображение на основе связанного с ним объявления.
     * @param ads
     * @return
     */
    Images findByAds(Ads ads);

    /**
     * Метод позволяет удалять изображения на основе связанного с ними объявления.
     * @param ads
     */
    void deleteByAds(Ads ads);
}
