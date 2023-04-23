package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

/**
 * Класс CommentServiceImpl представляет собой реализацию сервиса для работы с комментариями к объявлениям.
 */

@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsServiceImpl.class);
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              AdsRepository adsRepository,
                              UserRepository userRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * Метод addAdsComment обавляет новый комментарий к объявлению.
     * Метод принимает текст, дату и время нового комментария в виде объекта AdsCommentDto и идентификатор комментируемого объявления.
     * Создает новый комментарий на основе полученных данных, устанавливает связи с соответствующим объявлением
     * и автором комментария (авторизованным пользователем), сохраняет комментарий в репозитории,
     * обновляет список комментариев у соответствующего объявления и возвращает созданный комментарий в виде объекта AdsCommentDto.
     * @param adsCommentDto - текст, дата и время нового комментария.
     * @param adsId - идентификатор комментируемого объявления.
     * @return - комментарий к объвлению.
     */
    @Override
    public ResponseEntity<AdsCommentDto> addAdsComment(AdsCommentDto adsCommentDto, Long adsId) {
        LOGGER.info("Was invoked method for add comment for Ads.");
        adsCommentDto.setCreatedAt(Instant.now());
        Comment newComment = commentMapper.toComment(adsCommentDto);
        Ads commentedAds = adsRepository.findById(adsId).orElseThrow();
        newComment.setAds(commentedAds);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users author = userRepository.findByUsername(authentication.getName());
        newComment.setUsers(author);
        Comment savedComment = commentRepository.save(newComment);
        List<Comment> commentsOfAds = commentedAds.getComments();
        commentsOfAds.add(savedComment);
        commentedAds.setComments(commentsOfAds);
        AdsCommentDto returnedAdsCommentDto = commentMapper.toAdsCommentDto(savedComment);
        returnedAdsCommentDto.setAuthor(Math.toIntExact(author.getId()));
        return ResponseEntity.ok(returnedAdsCommentDto);
    }

    /**
     * Метод getAllCommentsOfAds получает все комментарии к объявлению.
     * Метод принимает идентификатор объявления, находит соответствующее объявление в репозитории,
     * получает список комментариев для этого объявления и преобразует их в список объектов AdsCommentDto.
     * Затем метод формирует и возвращает обертку ResponseWrapperAdsCommentDto,
     * содержащую количество комментариев и список комментариев в виде объекта AdsCommentDto.
     * @param adsId - идентификатор объявления.
     * @return - количество и список комментариев к объявлению.
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllCommentsOfAds(Long adsId) {
        LOGGER.info("Was invoked method for get all comments of Ads.");
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        List<Comment> commentsOfAds = commentRepository.findByAds(ads);
        List<AdsCommentDto> adsCommentDtoList = commentMapper.toListAdsCommentDto(commentsOfAds);
        for (int i = 0; i < adsCommentDtoList.size(); i++) {
            adsCommentDtoList.get(i).setAuthor(Math.toIntExact(commentsOfAds.get(i).getUsers().getId()));
        }
        ResponseWrapperAdsCommentDto wrapperCommentDto = new ResponseWrapperAdsCommentDto();
        wrapperCommentDto.setCount(adsCommentDtoList.size());
        wrapperCommentDto.setResults(adsCommentDtoList);
        return ResponseEntity.ok(wrapperCommentDto);
    }

    /**
     * Метод getAdsComment получает комментарий по его идентификатору.
     * Метод принимает идентификатор комментария, находит соответствующий комментарий в репозитории,
     * преобразует его в объект AdsCommentDto и возвращает его.
     * @param commentId - идентификатор комментария.
     * @return - найденный комментарий.
     */
    @Override
    public ResponseEntity<AdsCommentDto> getAdsComment(Long commentId) {
        LOGGER.info("Was invoked method for get comment by id.");
        Comment requiredComment = commentRepository.findById(commentId).orElseThrow();
        AdsCommentDto adsCommentDto = commentMapper.toAdsCommentDto(requiredComment);
        adsCommentDto.setAuthor(Math.toIntExact(requiredComment.getUsers().getId()));
        return ResponseEntity.ok(adsCommentDto);
    }

    /**
     * Метод getCommentsByAdsId получает комментарии по идентификатору объявления.
     * Метод принимает идентификатор объявления, находит соответствующее объявление в репозитории
     * и возвращает список комментариев для этого объявления.
     * @param adsId - идентификатор объявления.
     * @return - найденные комментарии.
     */
    @Override
    public List<Comment> getCommentsByAdsId(Long adsId) {
        LOGGER.info("Was invoked method for get comments by id of Ads.");
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        return commentRepository.findByAds(ads);
    }

    /**
     * Метод updateAdsComment редактирует комментарий.
     * Метод принимает идентификатор комментария и объект AdsCommentDto, содержащий изменения в комментарии.
     * Находит соответствующий комментарий в репозитории, обновляет его данные на основе полученных данных,
     * сохраняет комментарий в репозитории и возвращает обновленный комментарий в виде объекта AdsCommentDto.
     * @param commentId - идентификатор комментария.
     * @param adsCommentDto - изменения в комментарии.
     * @return - обновленный комментарий.
     */
    @Override
    public ResponseEntity<AdsCommentDto> updateAdsComment(Long commentId,
                                                          AdsCommentDto adsCommentDto) {
        LOGGER.info("Was invoked method for update comment.");
        Comment commentFromClient = commentMapper.toComment(adsCommentDto);
        Comment commentFromDataBase = commentRepository.findById(commentId).orElseThrow();
        commentFromDataBase.setCreatedAt(commentFromClient.getCreatedAt());
        if (commentFromClient.getText() != null) {
            commentFromDataBase.setText(commentFromClient.getText());
        }
        commentRepository.save(commentFromDataBase);
        AdsCommentDto returnedAdsCommentDto = commentMapper.toAdsCommentDto(commentFromDataBase);
        returnedAdsCommentDto.setAuthor(Math.toIntExact(commentFromDataBase.getUsers().getId()));
        return ResponseEntity.ok(returnedAdsCommentDto);
    }

    /**
     * Метод deleteAdsComment удаляет комментарий по его идентификатору.
     * Метод принимает идентификатор комментария, находит соответствующий комментарий в репозитории и удаляет его.
     * @param commentId - идентификатор объявления.
     */
    @Override
    public void deleteAdsComment(Long commentId) {
        LOGGER.info("Was invoked method for update comment.");
        commentRepository.deleteById(commentId);
    }
}
