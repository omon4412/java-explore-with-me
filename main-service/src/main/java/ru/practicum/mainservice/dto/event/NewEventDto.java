package ru.practicum.mainservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.model.Location;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO для создания нового события.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    /**
     * Идентификатор категории события.
     */
    @Positive
    @NotNull
    protected long category;

    /**
     * Аннотация события.
     */
    @NotBlank
    @Size(min = 20, max = 2000)
    protected String annotation;

    /**
     * Описание события.
     */
    @NotBlank
    @Size(min = 20, max = 7000)
    protected String description;

    /**
     * Дата и время события.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @NotNull
    protected LocalDateTime eventDate;

    /**
     * Местоположение события.
     */
    @Valid
    protected Location location;

    /**
     * Флаг оплаты события.
     */
    protected boolean paid;

    /**
     * Лимит участников события.
     */
    @PositiveOrZero
    @NotNull
    protected long participantLimit;

    /**
     * Флаг модерации запросов на участие в событии.
     */
    protected boolean requestModeration;

    /**
     * Название события.
     */
    @NotBlank
    @Size(min = 3, max = 120)
    protected String title;
}
