package ru.practicum.statisticservice.server;

import ru.practicum.statisticservice.dto.EndpointHitDto;
import ru.practicum.statisticservice.server.model.EndpointHit;

/**
 * Класс-маппер для преобразования между объектами {@link EndpointHit} и {@link EndpointHitDto}.
 */
public class EndPointMapper {

    /**
     * Преобразует объект {@link EndpointHit} в объект {@link EndpointHitDto}.
     *
     * @param endpointHit Объект {@link EndpointHit}, который требуется преобразовать.
     * @return Объект {@link EndpointHitDto}.
     */
    public static EndpointHitDto toEndPointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    /**
     * Преобразует объект {@link EndpointHitDto} в объект {@link EndpointHit}.
     *
     * @param endpointHitDto Объект {@link EndpointHitDto}, который требуется преобразовать.
     * @return Объект {@link EndpointHit}.
     */
    public static EndpointHit toEndPointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(endpointHitDto.getId())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
