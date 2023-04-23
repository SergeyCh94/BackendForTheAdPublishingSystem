package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс DTO используется для передачи данных объявлений между различными компонентами
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdsDto {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
    private String description;
}
