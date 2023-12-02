package ru.practicum.mainservice.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class NewCompilationDto {
    protected Set<Long> events;
    protected Boolean pinned;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    protected String title;
}
