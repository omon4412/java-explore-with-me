package ru.practicum.mainservice.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO для представления пользователя.
 */
@Data
public class NewUserRequest {
    /**
     * Имя пользователя.
     */
    @NotBlank
    @Size(min = 2, max = 250)
    protected String name;

    /**
     * Почта пользователя.
     */
    @NotBlank
    @Email(regexp = "^(.+)@(\\S+)$")
    @Size(min = 6, max = 254)
    protected String email;

    /**
     * Пароль пользователя.
     */
    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
    @Size(min = 6, max = 254)
    protected String password;
}
