package ru.practicum.mainservice.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * DTO для представления новой подборки.
 */
@Data
@Builder
public class NewCompilationDto {
    /**
     * Идентификаторы событий, которые надо добавить в подборку.
     */
    protected Set<Long> events;

    /**
     * Флаг, закрепить подборку или нет.
     */
    protected Boolean pinned;

    /**
     * Заголовок подборки.
     */
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    protected String title;
}
