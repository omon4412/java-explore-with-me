package ru.practicum.mainservice.controller.publicapi;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.service.CompilationService;

import java.util.Collection;

/**
 * Контроллер для обработки запросов по подборкам.
 */
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerPublic {
    /**
     * Сервис подборок.
     */
    private final CompilationService compilationService;

    /**
     * Обрабатывает HTTP-запрос GET для получения подборок категорий с пагинацией.
     *
     * @param pinned Флаг, фильтрующий закреплённые подборки.
     * @param from   Начальный индекс для пагинации.
     * @param size   Количество элементов на странице.
     * @return Ответ с коллекцией подборок и статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<Collection<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(compilationService.getCompilations(pinned, from, size), HttpStatus.OK);
    }

    /**
     * Обрабатывает HTTP-запрос GET для получения подборки по ее идентификатору.
     *
     * @param compId Идентификатор подборки, которую нужно получить.
     * @return Ответ со статусом 204 Ok.
     */
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable(name = "compId") Long compId) {
        return new ResponseEntity<>(compilationService.getCompilationById(compId), HttpStatus.OK);
    }
}
