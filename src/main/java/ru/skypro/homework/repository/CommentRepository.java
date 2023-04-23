package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;

import java.util.List;

/**
 * Интерфейс CommentRepository расширяет JpaRepository и предоставляет базовые CRUD-методы для работы с сущностью Comment в базе данных.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    /**
     * Метод findByAds(), позволяет искать комментарии на основе связанной с ними сущности Ads.
     * @param ads
     * @return
     */
    List<Comment> findByAds(Ads ads);
}
