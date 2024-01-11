package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.service.RequestService;

import java.util.Collection;

/**
 * Контроллер для обработки запросов на участие в событиях пользователей.
 */
@RestController
@RequestMapping(path = "/users/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {

    /**
     * Сервис, отвечающий за обработку операций, связанных с запросами на участие в событиях.
     */
    private final RequestService requestService;

    /**
     * Возвращает коллекцию всех запросов на участие пользователя.
     *
     * @return ResponseEntity, содержащий коллекцию всех запросов на участие пользователя.
     */
    @GetMapping
    public ResponseEntity<Collection<ParticipationRequestDto>> getAll(Authentication authentication) {
        return new ResponseEntity<>(requestService.getAllByUserRequests(authentication), HttpStatus.OK);
    }

    /**
     * Добавляет новый запрос на участие пользователя в указанное событие.
     *
     * @param eventId Идентификатор события, в которое подается запрос на участие.
     * @return ResponseEntity, содержащий добавленный запрос на участие.
     */
    @PostMapping
    public ResponseEntity<ParticipationRequestDto> add(Authentication authentication, @RequestParam(value = "eventId") long eventId) {
        return new ResponseEntity<>(requestService.addRequest(authentication, eventId), HttpStatus.CREATED);
    }

    /**
     * Отменяет запрос на участие пользователя в событии по его идентификатору.
     *
     * @param requestId Идентификатор запроса на участие, который отменяется.
     * @return ResponseEntity, содержащий отмененный запрос на участие.
     */
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancel(Authentication authentication, @PathVariable Long requestId) {
        return new ResponseEntity<>(requestService.cancelRequest(authentication, requestId), HttpStatus.OK);
    }
}

