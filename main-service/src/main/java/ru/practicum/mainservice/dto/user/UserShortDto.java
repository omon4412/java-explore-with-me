package ru.practicum.mainservice.dto.user;

import lombok.Builder;
import lombok.Data;

/**
 * DTO для представления пользователя.
 */
@Data
@Builder
public class UserShortDto {
    /**
     * Идентификатор пользователя.
     */
    protected long id;

    /**
     * Имя пользователя.
     */
    protected String name;
}
