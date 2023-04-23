package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Этот метод выполняет маппинг объекта "Comment" на объект "AdsCommentDto".
     * Аннотация "@Mapping" указывает соответствие между полями целевого класса "AdsCommentDto" и исходного класса "Comment".
     * Например, поле "pk" в "AdsCommentDto" маппится из поля "id" в "Comment".
     * @param comment
     * @return
     */
    @Mapping(target = "pk", source = "id")
    AdsCommentDto toAdsCommentDto(Comment comment);

    /**
     * Этот метод выполняет обратное маппинг объекта "AdsCommentDto" на объект "Comment".
     * Аннотация "@Mapping" указывает соответствие между полями целевого класса "Comment" и исходного класса "AdsCommentDto".
     * Например, поле "id" в "Comment" маппится из поля "pk" в "AdsCommentDto".
     * @param adsCommentDto
     * @return
     */
    @Mapping(target = "id",source = "pk")
    Comment toComment(AdsCommentDto adsCommentDto);

    /**
     * Этот метод выполняет маппинг списка объектов "Comment" на список объектов "AdsCommentDto".
     * MapStruct автоматически применяет маппер для каждого элемента списка.
     * @param commentsList
     * @return
     */
    List<AdsCommentDto> toListAdsCommentDto(List<Comment> commentsList);
}
