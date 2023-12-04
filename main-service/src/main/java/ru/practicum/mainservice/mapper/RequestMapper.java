package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.model.Request;

/**
 * Маппер для преобразования объектов сущности Request в соответствующие DTO.
 */
public class RequestMapper {

    private RequestMapper() {
    }

    /**
     * Преобразует сущность Request в объект ParticipationRequestDto.
     *
     * @param request Сущность Request.
     * @return Объект ParticipationRequestDto.
     */
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated().withNano(0))
                .status(request.getStatus().toString())
                .build();
    }
}
