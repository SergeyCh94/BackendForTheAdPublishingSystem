package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.avito.dto.AdsCommentDto;
import ru.skypro.avito.exception.CommentNotFoundException;
import ru.skypro.avito.exception.IncorrectArgumentException;
import ru.skypro.avito.mapper.AdsCommentMapperImplCustom;
import ru.skypro.avito.model.Comment;
import ru.skypro.avito.model.User;
import ru.skypro.avito.repository.CommentRepository;
import ru.skypro.avito.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final AdsServiceImpl adsService;

    @Override
    public List<AdsCommentDto> getComments(Integer id) {
        log.debug("Getting comments for ads with id: {}", id);
        return commentRepository.findAllByAdsId(id)
                .stream()
                .map(AdsCommentMapperImplCustom.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdsCommentDto addAdsComment(Integer id, AdsCommentDto adsCommentDto, Authentication authentication) {
        log.debug("Adding comment for ads with id: {}", id);

        if(adsCommentDto.getText() == null || adsCommentDto.getText().isBlank()) throw new IncorrectArgumentException();

        Comment comment = AdsCommentMapperImplCustom.INSTANCE.toEntity(adsCommentDto);
        User user = userService.getUserByUsername(authentication.getName());
        comment.setAuthor(user);
        comment.setAds(adsService.findAdsById(id));
        comment.setCreatedAt(Instant.now());
        commentRepository.save(comment);
        return AdsCommentMapperImplCustom.INSTANCE.toDto(comment);
    }

    @Override
    public void deleteAdsComment(Integer adId, Integer commentId) {
        log.debug("Deleting comment with id: {} for ads with id: {}", commentId, adId);
        Comment comment = getAdsComment(commentId, adId);
        commentRepository.delete(comment);
        log.info("Comment removed successfully");
    }

    @Override
    public AdsCommentDto updateComments(Integer adId, Integer commentId,
                                        AdsCommentDto adsCommentDto) {
        log.debug("Updating comment with id: {} for ads with id: {}", commentId, adId);

        if(adsCommentDto.getText() == null || adsCommentDto.getText().isBlank()) throw new IncorrectArgumentException();

        Comment adsComment = getAdsComment(commentId, adId);
        adsComment.setText(adsCommentDto.getText());
        commentRepository.save(adsComment);
        return AdsCommentMapperImplCustom.INSTANCE.toDto(adsComment);
    }

    public Comment getAdsComment(Integer commentId, Integer adId) {
        log.debug("Getting comment with id: {} for ads with id: {}", commentId, adId);
        return commentRepository.findByIdAndAdsId(commentId, adId).orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public Comment getCommentById(Integer id) {
        log.debug("Getting comment with id: {}", id);
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

}
