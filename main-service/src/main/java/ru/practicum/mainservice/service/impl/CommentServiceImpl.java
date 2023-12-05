package ru.practicum.mainservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CommentMapper;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.CommentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto commentDto) {
        User user = getOptionalUser(userId).get();
        Event event = getOptionalEvent(eventId).get();
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
        return resultCommentDto;
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto) {
        getOptionalUser(userId).get();
        Comment comment = getOptionalComment(commentId).get();
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

    private Optional<User> getOptionalUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", userId));
        }
        return userOptional;
    }

    private Optional<Event> getOptionalEvent(long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(String.format("Событие с ID=%d не найдено", eventId));
        }
        return eventOptional;
    }

    private Optional<Comment> getOptionalComment(long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            throw new NotFoundException(String.format("Комментарий с ID=%d не найден", commentId));
        }
        return commentOptional;
    }
}
