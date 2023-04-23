package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Контроллер для работы с объявлениями.
 */

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping("/ads")
public class AdsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);
    private final AdsService adsService;
    private final CommentService commentService;

    /**
     * Метод getALLAds() в данном случае обрабатывает HTTP GET-запрос на получение всех объявлений и возвращает объект ResponseEntity
     * с типом данных ResponseWrapperAdsDto в теле HTTP-ответа,
     * согласно описанию операции и возможных ответов, указанных в аннотациях @Operation и @ApiResponse.
     */
    @Operation(
            summary = "Получение всех объявлений", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping
    public ResponseEntity<ResponseWrapperAdsDto> getALLAds() {
        LOGGER.info("Was invoked method of AdsController for get All Ads.");
        return adsService.getAllAds();
    }

    /**
     * Метод addAds() обрабатывает HTTP POST-запрос на добавление нового объявления.
     * Он ожидает два параметра в теле запроса: properties, который передается в виде RequestPart и содержит данные нового объявления,
     * и image, который передается в виде MultipartFile и содержит изображение, связанное с объявлением.
     * Метод также принимает параметр authentication, который представляет аутентификационную информацию о текущем пользователе.
     *
     * @param createAdsDto - входная форма.
     * @param file         - изображение к объявлению.
     */
    @Operation(
            summary = "Добавление нового объявления", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AdsDto> addAds(@Valid
                                         @Parameter(description = "Передаем заполненное объявление")
                                         @RequestPart("properties") CreateAdsDto createAdsDto,
                                         @Parameter(description = "Передаем изображение к объявлению")
                                         @RequestPart("image") MultipartFile file,
                                         Authentication authentication) throws IOException {
        LOGGER.info("Was invoked method of AdsController for save Ads.");
        return adsService.saveAds(createAdsDto, file, authentication);
    }

    /**
     * Метод removeAds() обрабатывает HTTP DELETE-запрос на удаление объявления по его идентификатору (id).
     * Метод также содержит аннотацию @PreAuthorize,
     * которая выполняет проверку на авторизацию и права перед выполнением операции удаления.
     * @param id - идентификатор объявления.
     * @return - удаленное объявление.
     */
    @Operation(
            summary = "Удаление объявления по ID", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("@adsServiceImpl.getAdsById(#id).getBody().getEmail()" +
            "== authentication.principal.username or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AdsDto> removeAds(
            @Parameter(description = "Передает ID для удаления")
            @PathVariable Integer id) {
        LOGGER.info("Was invoked method of AdsController for delete Ads by id.");
        return adsService.removeAdsById(id.longValue());
    }

    /**
     * Метод getAds() обрабатывает HTTP GET-запрос на получение информации о конкретном объявлении по его идентификатору (id).
     * @param id - идентификатор объявления.
     * @return - найденное объявление.
     */
    @Operation(
            summary = "Получение объявления по идентификатору", description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = FullAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getAds(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id) {
        LOGGER.info("Was invoked method of AdsController for get Ads by id.");
        return adsService.getAdsById(id.longValue());
    }

    /**
     * Метод updateAds() обрабатывает HTTP PATCH-запрос на редактирование информации о конкретном объявлении по его идентификатору (id).
     * @param id - идентификатор редактируемого объявления.
     * @param adsDto - изменения в объявленгии.
     * @return - измененное объявление.
     */
    @Operation(
            summary = "Редактирование объявления", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("@adsServiceImpl.getAdsById(#id).getBody().getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id,
            @RequestBody AdsDto adsDto) {
        LOGGER.info("Was invoked method of AdsController for update Ads.");
        return adsService.updateAds(id.longValue(), adsDto);
    }

    /**
     * Метод getAdsMe() обрабатывает HTTP GET-запрос на получение списка объявлений, принадлежащих текущему авторизованному пользователю.
     * @param authentication - аутентификация текущего пользователя.
     * @return - список объявлений текущего пользователя.
     */
    @Operation(
            summary = "Получение своих объявлений", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        LOGGER.info("Was invoked method of AdsController for get Ads of current user.");
        return adsService.getAdsMe(authentication);
    }

    /**
     * Метод getAllCommentsOfAds() обрабатывает HTTP GET-запрос на получение всех комментариев для указанного объявления.
     * @param adsPk - идентификатор объявления.
     * @return - количество и список комментариев к объявлению.
     */
    @Operation(
            summary = "Получение всех комментариев объявления", description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsCommentDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping("/{adsPk}/comments")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllCommentsOfAds(
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk) {
        LOGGER.info("Was invoked method of AdsController for get all comments of Ads.");
        return commentService.getAllCommentsOfAds(adsPk.longValue());
    }

    /**
     * Метод addAdsComment() обрабатывает HTTP POST-запрос на добавление нового комментария к указанному объявлению.
     * @param adsCommentDto - текст, время и дата нового комментария.
     * @param adsPk - идентификатор комментируемого объявления.
     * @return - комментарий к объявлению.
     */
    @Operation(summary = "Добавление нового комментария к объявлению",
            description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsCommentDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping("/{adsPk}/comments")
    public ResponseEntity<AdsCommentDto> addAdsComment(
            @Parameter(description = "Передаем заполненный комментарий")
            @RequestBody AdsCommentDto adsCommentDto,
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk) {
        LOGGER.info("Was invoked method of AdsController for add comment for Ads.");
        return commentService.addAdsComment(adsCommentDto, adsPk.longValue());
    }

    /**
     * Метод updateAdsComment() обрабатывает HTTP PATCH-запрос на редактирование комментария текущим пользователем.
     * @param adsPk - идентификатор объявления.
     * @param id - идентификатор комментария.
     * @param adsCommentDto - изменения в комментарии.
     * @return - измененный комментарий.
     */
    @Operation(summary = "Редактирование комментария текущим пользователем", description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PreAuthorize("@userServiceImpl.getUser(@commentServiceImpl.getAdsComment(#id).getBody()" +
            ".getAuthor()).body.email == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{adsPk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(
            @PathVariable Integer adsPk,
            @PathVariable Integer id,
            @RequestBody AdsCommentDto adsCommentDto) {
        LOGGER.info("Was invoked method of AdsController for update comment.");
        if (!existCommentInAds(adsPk, id)) {
            return ResponseEntity.status(400).build();
        }
        return commentService.updateAdsComment(id.longValue(), adsCommentDto);
    }

    /**
     * Метод deleteAdsComment() обрабатывает HTTP DELETE-запрос на удаление комментария.
     * @param adsPk
     * @param id
     * @return
     */
    @Operation(summary = "Удаление комментария.", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PreAuthorize("@userServiceImpl.getUser(@commentServiceImpl.getAdsComment(#id).getBody()" +
            ".getAuthor()).body.email == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{adsPk}/comments/{id}")
    public ResponseEntity<Void> deleteAdsComment(
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "Передаем ID комментария")
            @PathVariable Integer id) {
        LOGGER.info("Was invoked method of AdsController for update comment.");
        if (!existCommentInAds(adsPk, id)) {
            return ResponseEntity.status(400).build();
        }
        commentService.deleteAdsComment(id.longValue());
        return ResponseEntity.status(200).build();
    }

    /**
     * Метод getAdsComment() обрабатывает HTTP GET-запрос для получения комментария по идентификаторам объявления и комментария.
     * @param adsPk - идентификатор объявления.
     * @param id - идентификатор комментария.
     * @return - найденный комментарий.
     */
    @Operation(summary = "Получение комментария по идентификаторам объявления и комментария",
            description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "Передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "Передаем ID комментария")
            @PathVariable Integer id) {
        LOGGER.info("Was invoked method of AdsController for get comment by adsId and commentId.");
        if (!existCommentInAds(adsPk, id)) {
            return ResponseEntity.status(400).build();
        }
        return commentService.getAdsComment(id.longValue());
    }

    /**
     * Метод updateAdsImage() обрабатывает HTTP PATCH-запрос для обновления изображения к объявлению.
     * @param id - идентификатор объявления.
     * @param image - новое изображение.
     * @param authentication - аутентификация текущего пользователя.
     * @return - Сообщение пользователю.
     */
    @Operation(
            summary = "Обновление изображения к объявлению", description = "", tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("@adsServiceImpl.getAdsById(#id).getBody().getEmail()" +
            "== authentication.principal.username or hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAdsImage(
            @Parameter(description = "Передаем ID объявления")
            @PathVariable Integer id,
            @Parameter(description = "Передаем новое изображение")
            @RequestPart(value = "image") @Valid MultipartFile image,
            Authentication authentication) throws IOException {
        LOGGER.info("Was invoked method of AdsController for update image of Ads.");
        adsService.updateAdsImage(id.longValue(), image);
        return ResponseEntity.ok().body("Изображение успешно обновлено.");
    }

    /**
     * Приватный метод existCommentInAds() выполняет проверку наличия комментария (commentId) в определенном объявлении (adsId).
     * @param adsId - идентификатор объявления.
     * @param commentId - идентификатор комментария.
     * @return
     */
    private Boolean existCommentInAds(Integer adsId, Integer commentId) {
        LOGGER.info("Was invoked method of AdsController for check adsId and commentId.");
        List<Comment> commentList = commentService.getCommentsByAdsId(adsId.longValue());
        boolean answer = false;
        for (Comment comment : commentList) {
            if (Math.toIntExact(comment.getId()) == commentId) {
                answer = true;
                break;
            }
        }
        return answer;
    }
}
