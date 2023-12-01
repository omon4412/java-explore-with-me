package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.model.Event;

/**
 * Маппер для преобразования объектов сущности Event в соответствующие DTO и наоборот.
 */
public class EventMapper {

    private EventMapper() {
    }

    /**
     * Преобразует сущность Event в объект FullEventDto.
     *
     * @param event Сущность Event.
     * @return Объект FullEventDto.
     */
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

    /**
     * Преобразует объект NewEventDto в сущность Event.
     *
     * @param newEventDto Объект NewEventDto.
     * @return Сущность Event.
     */
    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .participantLimit(newEventDto.getParticipantLimit())
                .paid(newEventDto.isPaid())
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .annotation(newEventDto.getAnnotation())
                .build();
    }

    /**
     * Преобразует сущность Event в объект EventShortDto.
     *
     * @param event Сущность Event.
     * @return Объект EventShortDto.
     */
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .build();
    }
}
