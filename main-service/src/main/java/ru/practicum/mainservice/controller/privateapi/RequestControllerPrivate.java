package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.service.RequestService;

import java.util.Collection;

/**
 * Контроллер для обработки запросов на участие в событиях пользователей.
 */
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {

    /**
     * Сервис, отвечающий за обработку операций, связанных с запросами на участие в событиях.
     */
    private final RequestService requestService;

    /**
     * Возвращает коллекцию всех запросов на участие пользователя.
     *
     * @param userId Идентификатор пользователя, для которого запрашиваются запросы на участие.
     * @return ResponseEntity, содержащий коллекцию всех запросов на участие пользователя.
     */
    @GetMapping
    public ResponseEntity<Collection<ParticipationRequestDto>> getAll(@PathVariable long userId) {
        return new ResponseEntity<>(requestService.getAllByUserRequests(userId), HttpStatus.OK);
    }

    /**
     * Добавляет новый запрос на участие пользователя в указанное событие.
     *
     * @param userId  Идентификатор пользователя, для которого добавляется запрос.
     * @param eventId Идентификатор события, в которое подается запрос на участие.
     * @return ResponseEntity, содержащий добавленный запрос на участие.
     */
    @PostMapping
    public ResponseEntity<ParticipationRequestDto> add(@PathVariable long userId, @RequestParam(value = "eventId") long eventId) {
        return new ResponseEntity<>(requestService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    /**
     * Отменяет запрос на участие пользователя в событии по его идентификатору.
     *
     * @param userId    Идентификатор пользователя, чей запрос отменяется.
     * @param requestId Идентификатор запроса на участие, который отменяется.
     * @return ResponseEntity, содержащий отмененный запрос на участие.
     */
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancel(@PathVariable long userId, @PathVariable Long requestId) {
        return new ResponseEntity<>(requestService.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}

