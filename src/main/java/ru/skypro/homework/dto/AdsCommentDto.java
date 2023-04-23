package ru.skypro.homework.dto;

import lombok.Data;

import java.time.Instant;

/**
 * Класс DTO для комментария к объявлению.
 */

@Data
public class AdsCommentDto {
    private Integer author;
    private Instant createdAt;
    private Integer pk;
    private String text;
}
