package ru.practicum.mainservice.controller.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.service.CommentService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Контроллер для обработки запросов администраторов по комментариям пользователей.
 */
@RestController
@RequestMapping(path = "${main-service.admin.path}/comments")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    /**
     * Сервис комментариев.
     */
    private final CommentService commentService;

    /**
     * HTTP Delete запрос на удаление комментария администратором.
     *
     * @param commentId Идентификатор комментария.
     * @return Ответ со статусом 204 No Content.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentToEvent(@PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * HTTP GET запрос на получение всех комментариев пользователя администратором.
     *
     * @param userId     Идентификатор пользователя.
     * @param text       Ключевое слово.
     * @param events     Список идентификаторов событий, в которых искать комментарии.
     * @param rangeStart Начало временного интервала для фильтрации комментариев.
     * @param rangeEnd   Конец временного интервала для фильтрации комментариев.
     * @param from       Индекс начального элемента для пагинации.
     * @param size       Размер страницы для пагинации.
     * @return ResponseEntity, содержащий список найденных комментариев.
     */
    @GetMapping
    public ResponseEntity<Collection<CommentDto>> getAllUserComments(@RequestParam Long userId,
                                                                     @RequestParam(required = false) String text,
                                                                     @RequestParam(required = false) List<Long> events,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                     @RequestParam(defaultValue = "0") int from,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Collection<CommentDto> comments = commentService.getAllUserComments(userId, text, events,
                rangeStart, rangeEnd, from, size);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
