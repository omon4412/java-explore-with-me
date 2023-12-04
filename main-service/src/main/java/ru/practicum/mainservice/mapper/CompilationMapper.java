package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.model.Compilation;

import java.util.stream.Collectors;

/**
 * Маппер для преобразования объектов {@link Compilation}, {@link NewCompilationDto} и {@link CompilationDto}.
 */
public class CompilationMapper {

    private CompilationMapper() {
    }

    /**
     * Преобразует объект {@link NewCompilationDto} в объект {@link Compilation}.
     *
     * @param compilationDto Объект {@link NewCompilationDto}
     * @return Объект {@link Compilation}
     */
    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned() != null)
                .build();
    }

    /**
     * Преобразует объект {@link Compilation} в объект {@link CompilationDto}.
     *
     * @param compilation Объект {@link Compilation}
     * @return Объект {@link CompilationDto}
     */
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEventsInCompilation().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
