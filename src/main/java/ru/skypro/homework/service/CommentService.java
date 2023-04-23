package ru.skypro.homework.service;


import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.model.Comment;

import java.util.List;

public interface CommentService {
    ResponseEntity<AdsCommentDto> addAdsComment(AdsCommentDto adsCommentDto, Long adsId);
    ResponseEntity<ResponseWrapperAdsCommentDto> getAllCommentsOfAds(Long adsId);
    ResponseEntity<AdsCommentDto> getAdsComment(Long commentId);
    List<Comment> getCommentsByAdsId(Long adsId);
    ResponseEntity<AdsCommentDto> updateAdsComment(Long commentId, AdsCommentDto adsCommentDto);
    void deleteAdsComment(Long commentId);
}
