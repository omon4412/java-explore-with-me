package ru.practicum.mainservice.dto.compilation;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * DTO для представления данных для обновления подборки.
 */
@Data
public class UpdateCompilationRequest {
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
    @Size(min = 1, max = 50)
    protected String title;
}
