package ru.practicum.mainservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CommentMapper;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.CommentService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto commentDto) {
        User user = getOptionalUser(userId).get();
        Event event = getOptionalEventIfPublished(eventId).get();
        Comment comment = CommentMapper.toComment(commentDto);
        if (commentDto.getParentCommentId() != null) {
            Comment parentComment = getOptionalComment(commentDto.getParentCommentId()).get();
            comment.setParentComment(parentComment);
        } else {
            if (userId.equals(event.getInitiator().getId())) {
                throw new ConflictException("Нельзя оставлять комментарий под своим событием, можно только отвечать");
            }
        }
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setChildComments(Collections.emptyList());
        comment.setCommentDate(LocalDateTime.now());
        Comment result = commentRepository.save(comment);
        CommentDto resultCommentDto = CommentMapper.toCommentDto(result);
        resultCommentDto.setIsEventAuthor(userId.equals(event.getInitiator().getId()));
        resultCommentDto.setLikeCount(0L);
        return resultCommentDto;
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto) {
        getOptionalUser(userId).get();
        Comment comment = getOptionalComment(commentId).get();
        getOptionalEventIfPublished(comment.getEvent().getId());
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("Нельзя редактировать чужой комментарий");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.minusHours(2).isAfter(comment.getCommentDate())) {
            throw new ConflictException("Редактировать комментарий можно в течении 2-ух часов после публикации");
        }
        comment.setText(commentDto.getText());
        comment.setUpdateDate(now);
        CommentDto resultCommentDto = CommentMapper.toCommentDto(commentRepository.save(comment));
        resultCommentDto.setIsEventAuthor(userId.equals(comment.getEvent().getInitiator().getId()));
        long likesCount = commentRepository.countLikesByComment(commentId);
        resultCommentDto.setLikeCount(likesCount);
        return resultCommentDto;
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        getOptionalUser(userId).get();
        Comment comment = getOptionalComment(commentId).get();
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("Нельзя удалить чужой комментарий");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.minusHours(2).isAfter(comment.getCommentDate())) {
            throw new ConflictException("Удалить комментарий можно в течении 2-ух часов после публикации");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        getOptionalComment(commentId).get();
        commentRepository.deleteById(commentId);
    }

    @Override
    public Collection<CommentDto> getAllUserComments(Long userId, String text, List<Long> events, LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd, int from, int size) {
        getOptionalUser(userId).get();
        Pageable pageable = PageRequest.of(from / size, size);
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException("Дата окончания не может быть раньше даты начала");
            }
        } else {
            rangeEnd = LocalDateTime.now();
        }
        if (text != null) {
            text = "%" + text.toLowerCase() + "%";
        }
        Collection<Comment> comments = commentRepository.getAllUserComments(userId, text, events, rangeStart,
                rangeEnd, pageable).getContent();

        return comments.stream()
                .map(c -> {
                    CommentDto commentDto = CommentMapper.toCommentDto(c);
                    commentDto.setIsEventAuthor(c.getAuthor().getId().equals(c.getEvent().getInitiator().getId()));
                    long likesCount = commentRepository.countLikesByComment(commentDto.getId());
                    commentDto.setLikeCount(likesCount);
                    return commentDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addLikeToComment(Long userId, Long commentId) {
        User user = getOptionalUser(userId).get();
        Comment comment = getOptionalComment(commentId).get();
        if (userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("Нельзя ставить лайк своему комментарию");
        }
        comment.getLikes().add(user);
        commentRepository.save(comment);
    }

    @Override
    public void removeLikeFromComment(Long userId, Long commentId) {
        User user = getOptionalUser(userId).get();
        Comment comment = getOptionalComment(commentId).get();
        if (comment.getLikes().contains(user)) {
            comment.getLikes().remove(user);
            commentRepository.save(comment);
        }
    }

    /**
     * Получает Optional<User> для заданного идентификатора пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Optional<User>, содержащий пользователя, если найден.
     * @throws NotFoundException Если пользователь с заданным идентификатором не найден.
     */
    private Optional<User> getOptionalUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", userId));
        }
        return userOptional;
    }

    /**
     * Получает Optional<Event> для заданного идентификатора события, только если оно опубликовано.
     *
     * @param eventId Идентификатор события.
     * @return Optional<Event>, содержащий событие, если оно опубликовано.
     * @throws NotFoundException Если событие с заданным идентификатором не найдено или не опубликовано.
     */
    private Optional<Event> getOptionalEventIfPublished(long eventId) {
        Optional<Event> eventOptional = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(String.format("Событие с ID=%d не найдено или не опубликовано", eventId));
        }
        return eventOptional;
    }

    /**
     * Получает Optional<Comment> для заданного идентификатора комментария.
     *
     * @param commentId Идентификатор комментария.
     * @return Optional<Comment>, содержащий комментарий, если найден.
     * @throws NotFoundException Если комментарий с заданным идентификатором не найден.
     */
    private Optional<Comment> getOptionalComment(long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            throw new NotFoundException(String.format("Комментарий с ID=%d не найден", commentId));
        }
        return commentOptional;
    }

}
