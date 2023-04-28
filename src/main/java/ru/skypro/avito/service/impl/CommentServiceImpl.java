package ru.skypro.avito.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.avito.dto.AdsCommentDto;
import ru.skypro.avito.exception.CommentNotFoundException;
import ru.skypro.avito.exception.IncorrectArgumentException;
import ru.skypro.avito.mapper.AdsCommentMapper;
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

    /**
     * Метод getComments() предполагает получение списка комментариев для объявления по его идентификатору.
     * @param id
     * @return
     */
    @Override
    public List<AdsCommentDto> getComments(Integer id) {
        // Выводится отладочное сообщение в лог о получении комментариев для объявления с указанным идентификатором.
        log.debug("Getting comments for ads with id: {}", id);

        // Используется репозиторий commentRepository для поиска комментариев в базе данных по идентификатору объявления
        // с использованием метода findAllByAdsId(id).
        return commentRepository.findAllByAdsId(id)

                // Список комментариев преобразуется в Stream, затем каждый комментарий маппится в объект AdsCommentDto
                // с использованием маппера AdsCommentMapper.INSTANCE::toDto.
                .stream()
                .map(AdsCommentMapper.INSTANCE::toDto)

                // Результат маппинга собирается обратно в список с использованием метода collect(Collectors.toList()).
                .collect(Collectors.toList());

        // Возвращается список AdsCommentDto, содержащий комментарии для указанного объявления.
    }

    /**
     * Метод addAdsComment() предполагает добавление комментария к объявлению с указанным идентификатором.
     * @param id
     * @param adsCommentDto
     * @param authentication
     * @return
     */
    @Override
    public AdsCommentDto addAdsComment(Integer id, AdsCommentDto adsCommentDto, Authentication authentication) {

        // Выводится отладочное сообщение в лог о добавлении комментария для объявления с указанным идентификатором.
        log.debug("Adding comment for ads with id: {}", id);

        // Проверяется, что текст комментария не является null или пустым, иначе выбрасывается исключение IncorrectArgumentException.
        if(adsCommentDto.getText() == null || adsCommentDto.getText().isBlank()) throw new IncorrectArgumentException();

        // Создается объект Comment на основе DTO комментария с использованием маппера AdsCommentMapper.INSTANCE.toEntity(adsCommentDto).
        Comment comment = AdsCommentMapper.INSTANCE.toEntity(adsCommentDto);

        // Получается текущий пользователь (автор комментария) из объекта Authentication, не извлекая его из БД,
        // с использованием метода getPrincipal() и приведения к объекту User.
        User user = (User) authentication.getPrincipal();

        // Устанавливается автор комментария в объект Comment с использованием метода setAuthor(user).
        // Устанавливается объявление, к которому добавляется комментарий, на основе его идентификатора с использованием сервиса adsService.findAdsById(id)
        // и метода setAds(ads).
        // Устанавливается текущее время в качестве времени создания комментария с использованием метода setCreatedAt(Instant.now()).
        comment.setAuthor(user);
        comment.setAds(adsService.findAdsById(id));
        comment.setCreatedAt(Instant.now());

        // Комментарий сохраняется в базе данных с использованием репозитория commentRepository и метода save(comment).
        commentRepository.save(comment);

        // Созданный комментарий преобразуется обратно в DTO с использованием маппера AdsCommentMapper.INSTANCE.toDto(comment).

        // Возвращается объект AdsCommentDto, содержащий добавленный комментарий.
        return AdsCommentMapper.INSTANCE.toDto(comment);
    }

    /**
     * Метод deleteAdsComment() предполагает удаление комментария с указанным идентификатором,
     * принадлежащего к объявлению с указанным идентификатором.
     * @param adId
     * @param commentId
     */
    @Override
    public void deleteAdsComment(Integer adId, Integer commentId) {

        // Выводится отладочное сообщение в лог о удалении комментария с указанным идентификатором для объявления с указанным идентификатором.
        log.debug("Deleting comment with id: {} for ads with id: {}", commentId, adId);

        // Вызывается метод getAdsComment(commentId, adId) для получения объекта комментария
        // на основе его идентификатора commentId и идентификатора объявления adId.
        Comment comment = getAdsComment(commentId, adId);

        // Удаление комментария из базы данных с использованием репозитория commentRepository и метода delete(comment).
        commentRepository.delete(comment);

        // Выводится информационное сообщение в лог о успешном удалении комментария.
        log.info("Comment removed successfully");
    }

    /**
     * Метод updateComments() предполагает обновление текста комментария с указанным идентификатором,
     * принадлежащего к объявлению с указанным идентификатором.
     * @param adId
     * @param commentId
     * @param adsCommentDto
     * @return
     */
    @Override
    public AdsCommentDto updateComments(Integer adId, Integer commentId,
                                        AdsCommentDto adsCommentDto) {

        // Выводится отладочное сообщение в лог о обновлении комментария с указанным идентификатором для объявления с указанным идентификатором.
        log.debug("Updating comment with id: {} for ads with id: {}", commentId, adId);

        // Проверяется, что текст комментария в adsCommentDto не является null или пустым значением.
        // Если текст комментария не прошел валидацию, выбрасывается исключение IncorrectArgumentException.
        if(adsCommentDto.getText() == null || adsCommentDto.getText().isBlank()) throw new IncorrectArgumentException();

        // Вызывается метод getAdsComment(commentId, adId) для получения объекта комментария
        // на основе его идентификатора commentId и идентификатора объявления adId.
        Comment adsComment = getAdsComment(commentId, adId);

        // Обновление текста комментария на значение из adsCommentDto.
        adsComment.setText(adsCommentDto.getText());

        // Сохранение обновленного комментария в базе данных с использованием репозитория commentRepository и метода save(adsComment).
        commentRepository.save(adsComment);

        // Возвращается объект комментария в формате DTO, преобразованный с использованием маппера AdsCommentMapper.INSTANCE.toDto(adsComment).
        return AdsCommentMapper.INSTANCE.toDto(adsComment);
    }

    /**
     * Метод getAdsComment() предназначен для получения объекта комментария на основе его идентификатора (commentId)
     * и идентификатора объявления (adId).
     * @param commentId
     * @param adId
     * @return
     */
    public Comment getAdsComment(Integer commentId, Integer adId) {

        // Выводится отладочное сообщение в лог о получении комментария с указанным идентификатором для объявления с указанным идентификатором.
        log.debug("Getting comment with id: {} for ads with id: {}", commentId, adId);

        // Вызывается метод findByIdAndAdsId(commentId, adId) репозитория commentRepository,
        // который ищет комментарий с указанным идентификатором (commentId) и идентификатором объявления (adId) в базе данных.
        // Если комментарий не найден, выбрасывается исключение CommentNotFoundException.
        // Если комментарий найден, он возвращается в виде объекта типа Comment.
        return commentRepository.findByIdAndAdsId(commentId, adId).orElseThrow(CommentNotFoundException::new);
    }

    /**
     * Метод getCommentById() предназначен для получения объекта комментария на основе его идентификатора (id).
     * @param id
     * @return
     */
    @Override
    public Comment getCommentById(Integer id) {
        // Выводится отладочное сообщение в лог о получении комментария с указанным идентификатором.
        log.debug("Getting comment with id: {}", id);

        // Вызывается метод findById(id) репозитория commentRepository,
        // который ищет комментарий с указанным идентификатором (id) в базе данных.
        // Если комментарий не найден, выбрасывается исключение CommentNotFoundException.
        // Если комментарий найден, он возвращается в виде объекта типа Comment.
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }
}
