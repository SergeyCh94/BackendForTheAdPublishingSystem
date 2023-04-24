package ru.skypro.avito.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.avito.dto.UserDto;
import ru.skypro.avito.model.Avatar;
import ru.skypro.avito.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

    String USER_AVATAR = "/users/avatar/";
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "image", source = "avatar", qualifiedByName = "avatarMapping")
    UserDto toDto(User user);

    @Named("avatarMapping")
    default String avatarMapping(Avatar avatar) {
        if (avatar == null) {
            return null;
        }
        return USER_AVATAR + avatar.getId();
    }

}
