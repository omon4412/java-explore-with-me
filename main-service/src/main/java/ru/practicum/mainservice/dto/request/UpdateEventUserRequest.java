package ru.practicum.mainservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.StateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Запрос на обновление информации о событии пользователем.
 */
@Data
public class UpdateEventUserRequest {
    /**
     * Категория события.
     */
    protected Long category;

    /**
     * Аннотация события.
     */
    @Size(min = 20, max = 2000)
    protected String annotation;

    /**
     * Описание события.
     */
    @Size(min = 20, max = 7000)
    protected String description;

    /**
     * Дата и время события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    protected LocalDateTime eventDate;

    /**
     * Местоположение события.
     */
    protected Location location;

    /**
     * Флаг оплаты события.
     */
    protected Boolean paid;

    /**
     * Лимит участников события.
     */
    @PositiveOrZero
    protected Long participantLimit;

    /**
     * Флаг модерации события.
     */
    protected Boolean requestModeration;

    /**
     * Название события.
     */
    @Size(min = 3, max = 120)
    protected String title;

    /**
     * Состояние события.
     */
    protected StateAction stateAction;
}
