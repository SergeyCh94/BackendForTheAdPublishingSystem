package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Avatar;

/**
 * Интерфейс AvatarRepository расширяет JpaRepository и предоставляет базовые CRUD-методы для работы с сущностью Avatar в базе данных.
 */
@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
