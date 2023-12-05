package ru.practicum.mainservice.controller.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.service.CommentService;

/**
 * Контроллер для обработки запросов администраторов по комментариям пользователей.
 */
@RestController
@RequestMapping(path = "${main-service.admin.path}/comments")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentToEvent(@PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
