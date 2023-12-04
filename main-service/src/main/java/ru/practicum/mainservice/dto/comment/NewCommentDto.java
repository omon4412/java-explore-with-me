package ru.practicum.mainservice.dto.comment;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewCommentDto {
    @NotNull
    @NotBlank
    @Size(min = 10, max = 1000)
    protected String text;
    protected Long parentCommentId;
}
