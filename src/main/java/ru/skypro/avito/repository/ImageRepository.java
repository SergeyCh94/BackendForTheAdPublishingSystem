package ru.skypro.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.avito.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
