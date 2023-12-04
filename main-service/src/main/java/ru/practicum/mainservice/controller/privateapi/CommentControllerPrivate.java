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
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addCommentToEvent(@PathVariable long userId,
                                                        @RequestParam(value = "eventId") Long eventId,
                                                        @Valid @RequestBody NewCommentDto commentDto) {
        return new ResponseEntity<>(commentService.addComment(userId, eventId, commentDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateCommentToEvent(@PathVariable long userId,
                                                           @Valid @RequestBody UpdateCommentDto commentDto,
                                                           @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.updateComment(userId, commentId, commentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentToEvent(@PathVariable long userId,
                                                     @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
