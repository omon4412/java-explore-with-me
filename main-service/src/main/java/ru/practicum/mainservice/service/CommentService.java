package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для управления комментариями.
 */
public interface CommentService {

    /**
     * Добавляет новый комментарий.
     *
     * @param userId     Идентификатор пользователя, оставляющего комментарий
     * @param eventId    Идентификатор события, к которому оставляется комментарий
     * @param commentDto Данные нового комментария
     * @return объект CommentDto с информацией о добавленном комментарии
     */
    CommentDto addComment(Long userId, Long eventId, NewCommentDto commentDto);

    /**
     * Обновляет существующий комментарий.
     *
     * @param userId     Идентификатор пользователя, редактирующего комментарий
     * @param commentId  Идентификатор редактируемого комментария
     * @param commentDto Данные для обновления комментария
     * @return объект CommentDto с информацией об обновленном комментарии
     */
    CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto);

    /**
     * Удаляет комментарий.
     *
     * @param userId    Идентификатор пользователя, удаляющего комментарий
     * @param commentId Идентификатор удаляемого комментария
     */
    void deleteComment(Long userId, Long commentId);

    /**
     * Удаляет комментарий администратором.
     *
     * @param commentId Идентификатор удаляемого комментария
     */
    void deleteCommentByAdmin(Long commentId);

    Collection<CommentDto> getAllUserComments(Long userId, String text, List<Long> events, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, int from, int size);
}
