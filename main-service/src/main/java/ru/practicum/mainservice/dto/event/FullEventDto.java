package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.model.Location;

import java.time.LocalDateTime;

/**
 * DTO для представления полной информации о событии.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullEventDto {
    /**
     * Идентификатор события.
     */
    protected long id;

    /**
     * Категория события.
     */
    protected CategoryDto category;

    /**
     * Аннотация события.
     */
    protected String annotation;

    /**
     * Инициатор события.
     */
    protected UserShortDto initiator;

    /**
     * Количество подтвержденных запросов.
     */
    protected Long confirmedRequests;

    /**
     * Описание события.
     */
    protected String description;

    /**
     * Дата и время события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;

    /**
     * Дата и время публикации события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime publishedOn;

    /**
     * Дата и время создания события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createdOn;

    /**
     * Местоположение события.
     */
    protected Location location;

    /**
     * Флаг оплаты события.
     */
    protected boolean paid;

    /**
     * Лимит участников события.
     */
    protected long participantLimit;

    /**
     * Флаг модерации события.
     */
    protected boolean requestModeration;

    /**
     * Название события.
     */
    protected String title;

    /**
     * Состояние события.
     */
    protected EventState state;

    /**
     * Количество просмотров события.
     */
    protected Long views;
}
