package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.service.EventService;

import javax.validation.Valid;

/**
 * Контроллер для обработки запросов по событиям.
 */
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;

    @PostMapping
    public FullEventDto addEvent(@PathVariable(value = "userId") long userId, @Valid @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(eventDto, userId);
    }
}
