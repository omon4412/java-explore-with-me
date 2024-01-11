package ru.practicum.mainservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO для создания нового комментария.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    /**
     * Текст нового комментария.
     */
    @NotNull
    @NotBlank
    @Size(min = 10, max = 1000)
    protected String text;

    /**
     * Идентификатор родительского комментария, если таковой имеется.
     */
    protected Long parentCommentId;
}
