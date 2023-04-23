package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Users;

import java.util.List;

/**
 * Интерфейс AdsRepository, который расширяет интерфейс JpaRepository и предоставляет методы для работы с репозиторием объявлений в базе данных.
 */
@Repository
public interface AdsRepository extends JpaRepository<Ads, Long> {

    /**
     * Поиск объявлений, принадлежащих определенному пользователю.
     * @param user
     * @return
     */
    List<Ads> findByUsers(Users user);

    /**
     * Поиск объявлений, содержащих указанную подстроку в заголовке.
     * @param partOfTitle
     * @return
     */
    List<Ads> findByTitleContains(String partOfTitle);

    /**
     * Поиск объявлений, содержащих указанную подстроку в описании.
     * @param partOfDescription
     * @return
     */
    List<Ads> findByDescriptionContains(String partOfDescription);

    /**
     * Поиск объявлений с указанной ценой.
     * @param price
     * @return
     */
    List<Ads> findByPrice(Integer price);

    /**
     * Поиск объявлений с ценой меньше или равной указанной.
     * @param maxPrice
     * @return
     */
    List<Ads> findByPriceLessThanEqual(Integer maxPrice);

    /**
     * Поиск объявлений с ценой больше или равной указанной.
     * @param minPrice
     * @return
     */
    List<Ads> findByPriceGreaterThanEqual(Integer minPrice);

    /**
     * Поиск объявлений с ценой в указанном диапазоне
     * @param minPrice
     * @param maxPrice
     * @return
     */
    List<Ads> findByPriceBetween(Integer minPrice, Integer maxPrice);

    /**
     * Поиск объявлений, содержащих указанные подстроки и в заголовке, и в описании.
     * @param partOfTitle
     * @param partOfDescription
     * @return
     */
    List<Ads> findByTitleContainsAndDescriptionContains(String partOfTitle, String partOfDescription);

    /**
     * Поиск объявлений, содержащих указанные подстроки в заголовке и описании, и с ценой меньше или равной указанной, отсортированных по цене.
     * @param partOfTitle
     * @param partOfDescription
     * @param maxPrice
     * @return
     */
    List<Ads> findByTitleContainsAndDescriptionContainsAndPriceLessThanEqualOrderByPrice(String partOfTitle,
                                                                                         String partOfDescription,
                                                                                         Integer maxPrice);

    /**
     * Поиск объявлений, содержащих указанные подстроки в заголовке и описании, и с ценой больше или равной указанной, отсортированных по цене.
     * @param partOfTitle
     * @param partOfDescription
     * @param minPrice
     * @return
     */
    List<Ads> findByTitleContainsAndDescriptionContainsAndPriceGreaterThanEqualOrderByPrice(String partOfTitle,
                                                                                            String partOfDescription,
                                                                                            Integer minPrice);

    /**
     * Поиск объявлений, содержащих указанные подстроки в заголовке и описании, и с ценой в указанном диапазоне.
     * @param partOfTitle
     * @param partOfDescription
     * @param minPrice
     * @param maxPrice
     * @return
     */
    List<Ads> findByTitleContainsAndDescriptionContainsAndPriceBetween(String partOfTitle,
                                                                       String partOfDescription,
                                                                       Integer minPrice,
                                                                       Integer maxPrice);
}
