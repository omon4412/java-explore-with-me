package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.service.CommentService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для обработки запросов по комментариям пользователей.
 */
@RestController
@RequestMapping(path = "/users/comments")
@RequiredArgsConstructor
public class CommentControllerPrivate {
    /**
     * Сервис комментариев.
     */
    private final CommentService commentService;

    /**
     * Добавление нового комментария к событию.
     *
     * @param eventId    Идентификатор события, к которому оставляют комментарий.
     * @param commentDto Комментарий.
     * @return ResponseEntity, содержащий добавленный комментарий к событию.
     */
    @PostMapping
    public ResponseEntity<CommentDto> addCommentToEvent(Authentication authentication,
                                                        @RequestParam(value = "eventId") Long eventId,
                                                        @Valid @RequestBody NewCommentDto commentDto) {
        return new ResponseEntity<>(commentService.addComment(authentication, eventId, commentDto), HttpStatus.CREATED);
    }

    /**
     * Редактирование своего комментария.
     *
     * @param commentDto Изменённый комментарий.
     * @param commentId  Идентификатор комментария, который редактируют.
     * @return ResponseEntity, содержащий обновлённый комментарий к событию.
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateCommentToEvent(Authentication authentication,
                                                           @Valid @RequestBody UpdateCommentDto commentDto,
                                                           @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.updateComment(authentication, commentId, commentDto), HttpStatus.OK);
    }

    /**
     * Удаление своего комментария.
     *
     * @param commentId Идентификатор комментария, который удаляют.
     * @return Ответ со статусом 204 No Content.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentToEvent(Authentication authentication,
                                                     @PathVariable Long commentId) {
        commentService.deleteComment(authentication, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Поставить лайк комментарию.
     *
     * @param commentId Идентификатор комментарий, которому ставят лайк.
     * @return Ответ со статусом 200 OK.
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Map<String, String>> likeComment(Authentication authentication,
                                                           @PathVariable Long commentId) {
        commentService.addLikeToComment(authentication, commentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OK");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Убрать лайк у комментария.
     *
     * @param commentId Идентификатор комментарий, у которого убирают лайк.
     * @return Ответ со статусом 200 OK.
     */
    @PostMapping("/{commentId}/unlike")
    public ResponseEntity<Map<String, String>> unlikeComment(Authentication authentication,
                                                             @PathVariable Long commentId) {
        commentService.removeLikeFromComment(authentication, commentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OK");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
