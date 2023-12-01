package ru.practicum.mainservice.dto.request;

import lombok.Data;

import java.util.List;

/**
 * Результат обновления статуса запросов на участие в событии.
 */
@Data
public class EventRequestStatusUpdateResult {

    /**
     * Список подтвержденных запросов на участие.
     */
    protected List<ParticipationRequestDto> confirmedRequests;

    /**
     * Список отклоненных запросов на участие.
     */
    protected List<ParticipationRequestDto> rejectedRequests;
}
