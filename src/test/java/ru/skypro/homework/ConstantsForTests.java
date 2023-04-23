package ru.skypro.homework;


import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;

public class ConstantsForTests {
    public static final Ads ADS_1 = new Ads(1L, "ru/skypro/homework/image", 1000, "title",
            "description", new Users(1L, "test@mail.ru", "Ivan",
            "Ivanov", "+7(999)777-55-33", Role.USER, "password", true,
            null, null, null, null), new Images(1L, "mediaType",
            "filePath",10000L, null), null);
    public static final Ads ADS_2 = new Ads(2L, "ru/skypro/homework/image", 2000, "title",
            "description", new Users(1L, "test@mail.ru", "Ivan",
            "Ivanov", "+7(999)777-55-33", Role.USER, "password", true,
            null, null, null, null), null, null);
    public static final AdsDto ADS_DTO_1 = new AdsDto(1, "ru/skypro/homework/image", 1, 1000,
            "title", "description");
    public static final AdsDto ADS_DTO_2 = new AdsDto(1, "ru/skypro/homework/image", 2, 2000,
            "title", "description");
    public static final CreateAdsDto CREATE_ADS_DTO = new CreateAdsDto("description",
            "ru/skypro/homework/image", 2, 200, "title");
    public static final Users USER_1 = new Users(1L, "admin@mail.ru", "Ivan",
            "Ivanov", "+7(999)777-55-33", Role.ADMIN, "Password2022",
            true, null, null, null, "ru/skypro/homework/image");
    public static final Users USER_2 = new Users(2L, "test2@mail.ru", "Petr",
            "Petrov", "+7(999)888-22-77", Role.USER, "password2", true,
            null, null, null, "ru/skypro/homework/image");
    public static final UserDto USER_DTO_1 = new UserDto(1, "Ivan", "Ivanov",
            "test@mail.ru", "+7(999)777-55-33", "ru/skypro/homework/image");
    public static final UserDto USER_DTO_2 = new UserDto(2, "Petr", "Petrov",
            "test2@mail.ru", "+7(999)888-22-77", "ru/skypro/homework/image");
    public static final String USER_NAME_1 = "test@mail.ru";
    public static final Images IMAGES = new Images(1L, "mediaType", "filePath",
            10000L, null);
}
