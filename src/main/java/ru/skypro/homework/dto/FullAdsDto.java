package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс DTO используется для передачи полных данных об объявлении
 */

@Data
public class FullAdsDto {

    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer pk;
    private Integer price;
    private String title;
}
