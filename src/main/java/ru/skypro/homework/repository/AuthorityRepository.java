package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Authority;

/**
 * Интерфейс AuthorityRepository расширяет JpaRepository и предоставляет базовые CRUD-методы для работы с сущностью Authority в базе данных.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
