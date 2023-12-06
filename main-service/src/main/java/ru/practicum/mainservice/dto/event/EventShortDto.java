package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.user.UserShortDto;

import java.time.LocalDateTime;

/**
 * DTO для представления полной информации о событии.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
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
     * Дата и время события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate;

    /**
     * Флаг оплаты события.
     */
    protected boolean paid;

    /**
     * Название события.
     */
    protected String title;

    /**
     * Количество просмотров события.
     */
    protected Long views;

    /**
     * Количество комментариев на событии.
     */
    protected Long commentsCount;
}
