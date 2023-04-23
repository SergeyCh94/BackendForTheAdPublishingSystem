package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.model.Users;

/**
 *  Интерфейс UserRepository расширяет JpaRepository и предоставляет базовые CRUD-методы для работы с сущностью Users в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    /**
     * Метод позволяет искать пользователя на основе его имени пользователя (username).
     * @param username
     * @return
     */
    Users findByUsername(String username);

    /**
     * Метод позволяет искать пользователя на основе его роли (role).
     * @param role
     * @return
     */
    Users findByRole(Role role);
}
