package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {

    /**
     * Сервис, отвечающий за обработку операций, связанных с событиями.
     */
    private final EventService eventService;

    /**
     * Создает новое событие указанным пользователем.
     *
     * @param userId   Идентификатор пользователя, который создается событие.
     * @param eventDto Данные нового события.
     * @return ResponseEntity, содержащий созданное событие.
     */
    @PostMapping
    public ResponseEntity<FullEventDto> addEvent(@PathVariable(value = "userId") long userId,
                                                 @Valid @RequestBody NewEventDto eventDto) {
        return new ResponseEntity<>(eventService.addEvent(eventDto, userId), HttpStatus.CREATED);
    }

    /**
     * Возвращает коллекцию краткой информации о событиях пользователя.
     *
     * @param userId Идентификатор пользователя, для которого запрашиваются события.
     * @param from   Индекс начального элемента для пагинации.
     * @param size   Размер страницы для пагинации.
     * @return ResponseEntity, содержащий коллекцию краткой информации о событиях пользователя.
     */
    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getUsersEvents(@PathVariable(value = "userId") long userId,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getUsersAllEvent(userId, from, size), HttpStatus.OK);
    }

    /**
     * Возвращает полную информацию о событии пользователя по его идентификатору.
     *
     * @param userId  Идентификатор пользователя, для которого запрашивается событие.
     * @param eventId Идентификатор события.
     * @return ResponseEntity, содержащий полную информацию о событии пользователя.
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<FullEventDto> getUsersEventById(@PathVariable(value = "userId") long userId,
                                                          @PathVariable(value = "eventId") long eventId) {
        return new ResponseEntity<>(eventService.getUserEventById(userId, eventId), HttpStatus.OK);
    }

    /**
     * Обновляет информацию о событии пользователя по его идентификатору.
     *
     * @param userId  Идентификатор пользователя, для которого обновляется событие.
     * @param eventId Идентификатор события.
     * @param request Запрос на обновление данных события пользователя.
     * @return ResponseEntity, содержащий обновленные данные о событии пользователя.
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<FullEventDto> updateUsersEventById(@PathVariable(value = "userId") long userId,
                                                             @PathVariable(value = "eventId") long eventId,
                                                             @Valid @RequestBody UpdateEventUserRequest request) {
        return new ResponseEntity<>(eventService.updateUsersEventById(userId, eventId, request), HttpStatus.OK);
    }

    /**
     * Возвращает коллекцию запросов на участие в событии пользователя.
     *
     * @param userId  Идентификатор пользователя, для которого запрашиваются запросы на участие.
     * @param eventId Идентификатор события.
     * @return ResponseEntity, содержащий коллекцию запросов на участие в событии пользователя.
     */
    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Collection<ParticipationRequestDto>> getUsersEventRequests(@PathVariable(value = "userId") long userId,
                                                                                     @PathVariable(value = "eventId") long eventId) {
        return new ResponseEntity<>(eventService.getUsersEventRequests(userId, eventId), HttpStatus.OK);
    }

    /**
     * Обновляет статус запросов на участие в событии пользователя.
     *
     * @param userId  Идентификатор пользователя, для которого обновляется статус запросов.
     * @param eventId Идентификатор события.
     * @param request Запрос на обновление статуса запросов.
     * @return ResponseEntity, содержащий результат обновления статуса запросов на участие.
     */
    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestsStatus(@PathVariable(value = "userId") long userId,
                                                                               @PathVariable(value = "eventId") long eventId,
                                                                               @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        return new ResponseEntity<>(eventService.changeRequestsStatus(userId, eventId, request), HttpStatus.OK);
    }
}

