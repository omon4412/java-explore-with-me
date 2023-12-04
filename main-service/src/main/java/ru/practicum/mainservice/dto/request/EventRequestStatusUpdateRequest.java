package ru.practicum.mainservice.dto.request;

import lombok.Data;
import ru.practicum.mainservice.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Dto для обновления статуса запросов на участие.
 */
@Data
public class EventRequestStatusUpdateRequest {

    /**
     * Список идентификаторов запросов на участие.
     */
    @NotNull
    protected List<Long> requestIds;

    /**
     * Новый статус для запросов на участие.
     */
    @NotNull
    protected RequestStatus status;
}
