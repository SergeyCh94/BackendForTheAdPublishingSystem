package ru.skypro.avito.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.avito.dto.AdsDto;
import ru.skypro.avito.dto.CreateAds;
import ru.skypro.avito.dto.FullAds;
import ru.skypro.avito.model.Ads;
import ru.skypro.avito.model.Image;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    String ADS_IMAGE = "/ads/image/";
    AdsMapper INSTANCE = Mappers.getMapper(AdsMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ads toEntity(CreateAds dto);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageMapping")
    AdsDto toDto(Ads ads);

    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "email", source = "author.username")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageMapping")
    @Mapping(target = "pk", source = "id")
    FullAds toFullAds(Ads ads);

    @Named("imageMapping")
    default String imageMapping(Image image) {
        if (image == null) {
            return null;
        }
        return ADS_IMAGE + image.getId();
    }

}
