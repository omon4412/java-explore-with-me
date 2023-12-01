package ru.practicum.mainservice.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для представления запросов на участие в событии.
 */
@Data
@Builder
public class ParticipationRequestDto {

    /**
     * Уникальный идентификатор запроса на участие.
     */
    private Long id;

    /**
     * Идентификатор связанного события.
     */
    private Long event;

    /**
     * Идентификатор пользователя, отправившего запрос.
     */
    private Long requester;

    /**
     * Дата и время создания запроса на участие.
     */
    private LocalDateTime created;

    /**
     * Статус запроса на участие.
     */
    private String status;
}
