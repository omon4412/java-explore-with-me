package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.NewCommentDto;
import ru.practicum.mainservice.dto.comment.UpdateCommentDto;
import ru.practicum.mainservice.model.Comment;

import java.util.stream.Collectors;

/**
 * Маппер для преобразования объектов {@link Comment}, {@link CommentDto} и {@link NewCommentDto}.
 */
public class CommentMapper {

    private CommentMapper() {
    }

    /**
     * Преобразует объект {@link NewCommentDto} в объект {@link Comment}.
     *
     * @param newCommentDto Объект {@link NewCommentDto}
     * @return Объект {@link Comment}
     */
    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }

    /**
     * Преобразует объект {@link UpdateCommentDto} в объект {@link Comment}.
     *
     * @param updateCommentDto Объект {@link UpdateCommentDto}
     * @return Объект {@link Comment}
     */
    public static Comment toComment(UpdateCommentDto updateCommentDto) {
        return Comment.builder()
                .text(updateCommentDto.getText())
                .build();
    }

    /**
     * Преобразует объект {@link Comment} в объект {@link CommentDto}.
     *
     * @param comment Объект {@link Comment}
     * @return Объект {@link CommentDto}
     */
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .childComments(comment.getChildComments().stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()))
                .commentDate(comment.getCommentDate())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .updateDate(comment.getUpdateDate())
                .build();
    }
}
