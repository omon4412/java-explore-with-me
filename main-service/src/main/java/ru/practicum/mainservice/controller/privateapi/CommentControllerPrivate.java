package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.service.CommentService;

import javax.validation.Valid;

/**
 * Контроллер для обработки запросов по комментариям пользователей.
 */
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentControllerPrivate {
    /**
     * Сервис комментариев.
     */
    private final CommentService commentService;

    /**
     * Добавление нового комментария к событию.
     *
     * @param userId     Идентификатор пользователя, который оставляет комментарий.
     * @param eventId    Идентификатор события, к которому оставляют комментарий.
     * @param commentDto Комментарий.
     * @return ResponseEntity, содержащий добавленный комментарий к событию.
     */
    @PostMapping
    public ResponseEntity<CommentDto> addCommentToEvent(@PathVariable long userId,
                                                        @RequestParam(value = "eventId") Long eventId,
                                                        @Valid @RequestBody NewCommentDto commentDto) {
        return new ResponseEntity<>(commentService.addComment(userId, eventId, commentDto), HttpStatus.CREATED);
    }

    /**
     * Редактирование своего комментария.
     *
     * @param userId     Идентификатор пользователя, который редактирует комментарий.
     * @param commentDto Изменённый комментарий.
     * @param commentId  Идентификатор комментария, который редактируют.
     * @return ResponseEntity, содержащий обновлённый комментарий к событию.
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateCommentToEvent(@PathVariable long userId,
                                                           @Valid @RequestBody UpdateCommentDto commentDto,
                                                           @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.updateComment(userId, commentId, commentDto), HttpStatus.OK);
    }

    /**
     * Удаление своего комментария.
     *
     * @param userId    Идентификатор пользователя, который удаляет комментарий.
     * @param commentId Идентификатор комментария, который удаляют.
     * @return Ответ со статусом 204 No Content.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentToEvent(@PathVariable long userId,
                                                     @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
