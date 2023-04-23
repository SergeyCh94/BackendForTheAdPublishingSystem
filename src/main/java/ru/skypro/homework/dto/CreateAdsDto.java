package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс DTO используется для передачи данных для создания нового объявления
 */

@Data
@AllArgsConstructor
public class CreateAdsDto {
    private String description;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
}
