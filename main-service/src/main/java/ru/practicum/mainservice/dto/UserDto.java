package ru.practicum.mainservice.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * DTO для представления пользователя.
 */
@Data
@Builder
public class UserDto {
    /**
     * Идентификатор пользователя.
     */
    protected long id;

    /**
     * Имя пользователя.
     */
    @NotBlank
    protected String name;

    /**
     * Почта пользователя.
     */
    @NotBlank
    protected String email;
}
