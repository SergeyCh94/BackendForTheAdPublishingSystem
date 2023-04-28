package ru.skypro.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.avito.model.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
}
