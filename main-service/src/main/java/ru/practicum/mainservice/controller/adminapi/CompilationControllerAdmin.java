package ru.practicum.mainservice.controller.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.service.CompilationService;

import javax.validation.Valid;

/**
 * Контроллер для обработки запросов от админа по подборкам.
 */
@RestController
@RequestMapping(path = "${main-service.admin.path}/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerAdmin {
    /**
     * Сервис подборок.
     */
    private final CompilationService compilationService;

    /**
     * Обрабатывает HTTP-запрос POST для создания новой подборки.
     *
     * @param newCompilationDto Данные новой подборки.
     * @return Ответ с созданной подборкой и статусом 201 Created.
     */
    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.addCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    /**
     * Обрабатывает HTTP-запрос DELETE для удаления подборки по ее идентификатору.
     *
     * @param compId Идентификатор подборки, которую нужно удалить.
     * @return Ответ со статусом 204 No Content.
     */
    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable(name = "compId") Long compId) {
        compilationService.deleteCompilationById(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Обрабатывает HTTP-запрос PATCH для обновления существующей подборки.
     *
     * @param compilationRequest Данные для обновления подборки.
     * @param compId             Идентификатор подборки, которую нужно обновить.
     * @return Ответ с обновленной подборкой и статусом 200 OK.
     */
    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@Valid @RequestBody
                                                            UpdateCompilationRequest compilationRequest,
                                                            @PathVariable Long compId) {
        return new ResponseEntity<>(compilationService.updateCompilation(compId, compilationRequest), HttpStatus.OK);
    }
}
