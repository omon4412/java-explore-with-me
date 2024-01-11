package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.UpdateEventUserRequest;
import ru.practicum.mainservice.service.EventService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Контроллер для обработки запросов по событиям пользователей.
 */
@RestController
@RequestMapping(path = "/users/events")
@RequiredArgsConstructor
public class EventControllerPrivate {

    /**
     * Сервис, отвечающий за обработку операций, связанных с событиями.
     */
    private final EventService eventService;

    /**
     * Создает новое событие указанным пользователем.
     *
     * @param eventDto Данные нового события.
     * @return ResponseEntity, содержащий созданное событие.
     */
    @PostMapping
    public ResponseEntity<FullEventDto> addEvent(Authentication authorization,
                                                 @Valid @RequestBody NewEventDto eventDto) {
        return new ResponseEntity<>(eventService.addEvent(eventDto, authorization), HttpStatus.CREATED);
    }

    /**
     * Возвращает коллекцию краткой информации о событиях пользователя.
     *
     * @param from Индекс начального элемента для пагинации.
     * @param size Размер страницы для пагинации.
     * @return ResponseEntity, содержащий коллекцию краткой информации о событиях пользователя.
     */
    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getUsersEvents(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                    Authentication authentication) {
        return new ResponseEntity<>(eventService.getUsersAllEvent(authentication, from, size), HttpStatus.OK);
    }

    /**
     * Возвращает полную информацию о событии пользователя по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return ResponseEntity, содержащий полную информацию о событии пользователя.
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<FullEventDto> getUsersEventById(Authentication authentication,
                                                          @PathVariable(value = "eventId") long eventId) {
        return new ResponseEntity<>(eventService.getUserEventById(authentication, eventId), HttpStatus.OK);
    }

    /**
     * Обновляет информацию о событии пользователя по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param request Запрос на обновление данных события пользователя.
     * @return ResponseEntity, содержащий обновленные данные о событии пользователя.
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<FullEventDto> updateUsersEventById(Authentication authentication,
                                                             @PathVariable(value = "eventId") long eventId,
                                                             @Valid @RequestBody UpdateEventUserRequest request) {
        return new ResponseEntity<>(eventService.updateUsersEventById(authentication, eventId, request), HttpStatus.OK);
    }

    /**
     * Возвращает коллекцию запросов на участие в событии пользователя.
     *
     * @param eventId Идентификатор события.
     * @return ResponseEntity, содержащий коллекцию запросов на участие в событии пользователя.
     */
    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Collection<ParticipationRequestDto>> getUsersEventRequests(Authentication authentication,
                                                                                     @PathVariable(value = "eventId") long eventId) {
        return new ResponseEntity<>(eventService.getUsersEventRequests(authentication, eventId), HttpStatus.OK);
    }

    /**
     * Обновляет статус запросов на участие в событии пользователя.
     *
     * @param eventId Идентификатор события.
     * @param request Запрос на обновление статуса запросов.
     * @return ResponseEntity, содержащий результат обновления статуса запросов на участие.
     */
    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestsStatus(Authentication authentication,
                                                                               @PathVariable(value = "eventId") long eventId,
                                                                               @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        return new ResponseEntity<>(eventService.changeRequestsStatus(authentication, eventId, request), HttpStatus.OK);
    }
}

