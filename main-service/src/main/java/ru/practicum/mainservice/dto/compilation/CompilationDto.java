package ru.practicum.mainservice.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.dto.event.EventShortDto;

import java.util.Set;

/**
 * DTO для представления данных подборки.
 */
@Data
@Builder
public class CompilationDto {
    /**
     * Идентификатор подборки.
     */
    protected Long id;

    /**
     * События, находящиеся в подборке.
     */
    protected Set<EventShortDto> events;

    /**
     * Флаг, закреплена подборка или нет.
     */
    protected Boolean pinned;

    /**
     * Заголовок подборки.
     */
    protected String title;
}
