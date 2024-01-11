package ru.practicum.mainservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * DTO для представления пользователя.
 */
@Data
@Builder
@AllArgsConstructor
public class UserDto implements UserInfo {
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
