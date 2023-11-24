package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.model.Event;

public class EventMapper {
    private EventMapper() {
    }

    public static FullEventDto toFullEventDto(Event event) {
        return FullEventDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .paid(event.isPaid())
                .description(event.getDescription())
                .title(event.getTitle())
                .createdOn(event.getCreatedOn())
                .requestModeration(event.isRequestModeration())
                .publishedOn(event.getPublishedOn())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .participantLimit(newEventDto.getParticipantLimit())
                .paid(newEventDto.isPaid())
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .requestModeration(newEventDto.isRequestModeration())
                .annotation(newEventDto.getAnnotation())
                .build();
    }
}
