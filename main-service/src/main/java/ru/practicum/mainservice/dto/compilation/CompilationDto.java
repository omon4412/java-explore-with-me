package ru.practicum.mainservice.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.dto.event.EventShortDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {
    protected Long id;
    protected Set<EventShortDto> events;
    protected Boolean pinned;
    protected String title;
}
