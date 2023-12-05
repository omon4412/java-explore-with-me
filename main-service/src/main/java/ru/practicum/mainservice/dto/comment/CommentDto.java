package ru.practicum.mainservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.dto.user.UserShortDto;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * DTO для представления комментария.
 */
@Data
@Builder
public class CommentDto {

    /**
     * Идентификатор комментария.
     */
    protected Long id;

    /**
     * Автор комментария.
     */
    protected UserShortDto author;

    /**
     * Флаг, указывающий, является ли автор комментария автором события.
     */
    protected Boolean isEventAuthor;

    /**
     * Текст комментария.
     */
    protected String text;

    /**
     * Дочерние комментарии.
     */
    protected Collection<CommentDto> childComments;

    /**
     * Дата создания комментария.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime commentDate;

    /**
     * Дата последнего обновления комментария.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateDate;
}
