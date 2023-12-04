package ru.practicum.mainservice.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentDto {
    @NotNull
    @NotBlank
    @Size(min = 10, max = 1000)
    protected String text;
}
