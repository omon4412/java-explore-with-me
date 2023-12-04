package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;

import java.util.Collection;

/**
 * Сервис для работы с подборками.
 */
public interface CompilationService {

    /**
     * Добавляет новую подборку.
     *
     * @param newCompilationDto DTO с данными новой подборки
     * @return DTO с добавленной подборкой
     */
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    /**
     * Удаляет подборку по её идентификатору.
     *
     * @param compId Идентификатор подборки для удаления
     */
    void deleteCompilationById(Long compId);

    /**
     * Получает коллекцию подборок с учётом параметров.
     *
     * @param pinned Флаг закреплённых подборок
     * @param from   Начальный индекс для пагинации
     * @param size   Количество элементов для пагинации
     * @return Коллекция подборок
     */
    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * Получает подборку по её идентификатору.
     *
     * @param compId Идентификатор подборки
     * @return DTO с данными подборки
     */
    CompilationDto getCompilationById(Long compId);

    /**
     * Обновляет подборку по её идентификатору.
     *
     * @param compId             Идентификатор подборки для обновления
     * @param compilationRequest DTO с данными для обновления подборки
     * @return Обновлённая подборка
     */
    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationRequest);
}
