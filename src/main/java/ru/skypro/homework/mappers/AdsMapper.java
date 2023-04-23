package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Users;

import java.util.List;

/**
 * Интерфейс AdsMapper, использует фреймворк MapStruct для маппинга (преобразования) объектов между различными классами (Ads, AdsDto, CreateAdsDto, FullAdsDto, Users).
 * Аннотация @Mapper указывает, что этот интерфейс является маппером и будет сгенерирован реализацией MapStruct во время компиляции.
 */

@Mapper(componentModel = "spring")
public interface AdsMapper {

    /**
     * Метод, который преобразует объект Ads в объект AdsDto.
     * Аннотация @Mapping указывает на соответствие между полями pk и id в исходном и целевом объектах соответственно.
     * @param ads
     * @return
     */
    @Mapping(target = "pk", source = "id")
    AdsDto toAdsDto(Ads ads);

    /**
     * Метод, который преобразует объект AdsDto в объект Ads.
     * Аннотация @Mapping указывает на соответствие между полями id и pk в исходном и целевом объектах соответственно.
     * @param adsDto
     * @return
     */
    @Mapping(target = "id", source = "pk")
    Ads toAds(AdsDto adsDto);

    /**
     * Метод, который преобразует объект CreateAdsDto в объект Ads без указания соответствия полей.
     * Предполагается, что поля description, image, pk, price и title в CreateAdsDto имеют соответствующие поля в Ads
     * и маппинг будет выполнен на основе их имен.
     * @param createAdsDto
     * @return
     */
    @Mapping(target = "id", source = "pk")
    Ads createAdsDtoToAds(CreateAdsDto createAdsDto);

    /**
     * Метод, который преобразует объекты Ads и Users в объект FullAdsDto.
     * Аннотации @Mapping указывают на соответствие между полями id и pk в объекте ads,
     * а также между полями firstName, lastName и username в объекте user и полями authorFirstName, authorLastName и email соответственно в объекте FullAdsDto.
     * @param ads
     * @param user
     * @return
     */
    @Mapping(target = "pk", source = "ads.id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "image", source = "ads.image")
    FullAdsDto toFullAdsDto(Ads ads, Users user);

    /**
     * Метод, который преобразует список объектов Ads в список объектов AdsDto.
     * Маппинг будет выполнен автоматически на основе метода toAdsDto для каждого элемента списка adsList.
     * @param adsList
     * @return
     */
    List<AdsDto> listAdsToListAdsDto(List<Ads> adsList);
}
