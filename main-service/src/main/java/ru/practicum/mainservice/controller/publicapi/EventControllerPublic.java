package ru.practicum.mainservice.controller.publicapi;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.model.SortTypes;
import ru.practicum.mainservice.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Контроллер для обработки запросов на события.
 */
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    /**
     * Сервис, отвечающий за обработку операций, связанных с событиями.
     */
    private final EventService eventService;

    /**
     * Возвращает полную информацию о событии для публичного доступа.
     *
     * @param eventId Идентификатор события.
     * @param request HTTP-запрос для получения информации о клиенте.
     * @return ResponseEntity, содержащий полную информацию о событии для публичного доступа.
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<FullEventDto> getEventPublic(@PathVariable long eventId, HttpServletRequest request) {
        return new ResponseEntity<>(eventService.getEventPublic(eventId, request), HttpStatus.OK);
    }

    /**
     * Поиск общедоступных событий с учетом различных критериев.
     *
     * @param text          Текст для поиска по событиям.
     * @param categories    Список идентификаторов категорий для фильтрации событий.
     * @param paid          Флаг указывающий, являются ли события платными.
     * @param rangeStart    Начало временного интервала для фильтрации событий.
     * @param rangeEnd      Конец временного интервала для фильтрации событий.
     * @param onlyAvailable Флаг указывающий, отображать только доступные события.
     * @param sort          Тип сортировки событий.
     * @param from          Индекс начального элемента для пагинации.
     * @param size          Размер страницы для пагинации.
     * @param request       HTTP-запрос для получения информации о клиенте.
     * @return ResponseEntity, содержащий коллекцию общедоступных событий.
     */
    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> searchEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        SortTypes sortType = SortTypes.EVENT_DATE;
        if (SortTypes.contains(sort)) {
            sortType = SortTypes.valueOf(sort);
        }
        Collection<EventShortDto> events = eventService.searchEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sortType, from, size, request);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}