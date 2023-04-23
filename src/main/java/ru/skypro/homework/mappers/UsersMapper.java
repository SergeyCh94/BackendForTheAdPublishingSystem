package ru.skypro.homework.mappers;

import org.mapstruct.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Users;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    /**
     *  Этот метод выполняет маппинг объекта "Users" на объект "UserDto".
     *  Аннотация "@Mapping" указывает соответствие между полями целевого класса "UserDto" и исходного класса "Users".
     *  Например, поле "email" в "UserDto" маппится из поля "username" в "Users".
     * @param user
     * @return
     */
    @Mapping(target = "email", source = "username")
    UserDto userToUserDto(Users user);

    /**
     * Этот метод выполняет обратное маппинг объекта "UserDto" на объект "Users".
     * Аннотация "@Mapping" не указана, поскольку MapStruct автоматически выполняет обратный маппинг для полей с совпадающими именами.
     * @param userDto
     * @return
     */
    Users userDtoToUser (UserDto userDto);

    /**
     * Этот метод выполняет маппинг объекта "RegisterReqDto" на объект "Users".
     * Аннотация "@Mapping" не указана, поскольку MapStruct автоматически выполняет маппинг полей с совпадающими именами.
     * @param registerReqDto
     * @return
     */
    Users registerReqDtoToUser(RegisterReqDto registerReqDto);

    /**
     * Этот метод выполняет маппинг списка объектов "Users" на список объектов "UserDto".
     * MapStruct автоматически применяет маппер для каждого элемента списка.
     * @param usersList
     * @return
     */
    List<UserDto> listUsersToListUserDto(List<Users> usersList);

    /**
     *  Этот метод обновляет объект "Users" на основе объекта "UserDto".
     *  Аннотация "@BeanMapping" указывает стратегию обработки null-значений.
     *  Аннотация "@MappingTarget" указывает целевой объект, который будет обновлен на основе исходного объекта "UserDto".
     * @param userDto
     * @param user
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget Users user);

    /**
     * Этот метод обновляет пароль объекта "Users" на основе объекта "NewPasswordDto".
     * Аннотация "@Mapping" указывает соответствие между полем "password" в "Users" и полем "newPassword" в "NewPasswordDto".
     * Аннотация "@MappingTarget" указывает целевой объект, который будет обновлен на основе исходного объекта "NewPasswordDto".
     * @param newPasswordDto
     * @param user
     */
    @Mapping(target = "password",source = "newPassword")
    void updatePassword(NewPasswordDto newPasswordDto, @MappingTarget Users user);
}
