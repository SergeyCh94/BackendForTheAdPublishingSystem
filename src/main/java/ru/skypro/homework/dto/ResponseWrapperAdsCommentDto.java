package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

/**
 * Класс ResponseWrapperAdsCommentDto представляет пример обертки для ответа (response wrapper) на запрос,
 * связанный с комментариями к объявлениям
 */

@Data
public class ResponseWrapperAdsCommentDto {
    private Integer count;
    private List<AdsCommentDto> results;
}
