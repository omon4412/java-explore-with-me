package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;

public interface EventService {

    FullEventDto addEvent(NewEventDto eventDto, long userId);


}
