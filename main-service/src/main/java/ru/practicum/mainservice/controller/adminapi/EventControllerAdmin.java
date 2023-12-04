package ru.practicum.mainservice.controller.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.request.UpdateEventAdminRequest;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс контроллера для обработки административных операций, связанных с мероприятиями.
 */
@RestController
@RequestMapping(path = "${main-service.admin.path}/events")
@RequiredArgsConstructor
public class EventControllerAdmin {

    /**
     * Сервис, ответственный за обработку операций, связанных с мероприятиями.
     */
    private final EventService eventService;

    /**
     * Конечная точка для обновления мероприятия администратором.
     *
     * @param eventId Уникальный идентификатор мероприятия для обновления.
     * @param request Тело запроса, содержащее данные для обновления мероприятия.
     * @return ResponseEntity, содержащий обновленные данные мероприятия.
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<FullEventDto> updateEventByAdmin(@PathVariable long eventId,
                                                           @Valid @RequestBody UpdateEventAdminRequest request) {
        return new ResponseEntity<>(eventService.updateEventByAdmin(eventId, request), HttpStatus.OK);
    }

    /**
     * Конечная точка для поиска мероприятий на основе указанных критериев.
     *
     * @param users      Список идентификаторов пользователей для фильтрации мероприятий по связанным пользователям.
     * @param states     Список состояний мероприятий для фильтрации мероприятий по состоянию.
     * @param categories Список идентификаторов категорий для фильтрации мероприятий по категории.
     * @param rangeStart Начало временного интервала для фильтрации мероприятий.
     * @param rangeEnd   Конец временного интервала для фильтрации мероприятий.
     * @param from       Индекс начального элемента для пагинации.
     * @param size       Размер страницы для пагинации.
     * @return ResponseEntity, содержащий список найденных мероприятий.
     */
    @GetMapping
    public ResponseEntity<Collection<FullEventDto>> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<EventState> eventStates = null;
        if (states != null) {
            eventStates = states.stream()
                    .filter(EventState::contains)
                    .map(EventState::valueOf)
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(eventService.searchEvents(users, eventStates, categories,
                rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }
}
