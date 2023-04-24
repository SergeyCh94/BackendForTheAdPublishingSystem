package ru.skypro.avito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.avito.model.Ads;

import java.util.List;


@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    List<Ads> findAllByAuthorId(Integer id);

}

