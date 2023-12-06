package ru.practicum.mainservice.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO для обновления комментария.
 */
@Data
public class UpdateCommentDto {
    /**
     * Новый текст комментария.
     */
    @NotNull
    @NotBlank
    @Size(min = 10, max = 1000)
    protected String text;
}
