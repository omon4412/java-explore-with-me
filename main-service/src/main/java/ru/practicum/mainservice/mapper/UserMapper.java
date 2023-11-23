package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.NewUserRequest;
import ru.practicum.mainservice.dto.UserDto;
import ru.practicum.mainservice.model.User;

/**
 * Маппер для преобразования объектов {@link User}, {@link UserDto} и {@link NewUserRequest}.
 */
public class UserMapper {
    private UserMapper() {
    }

    /**
     * Преобразует объект {@link UserDto} в объект {@link User}.
     *
     * @param userDto Объект {@link UserDto}
     * @return Объект {@link User}
     */
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Преобразует объект {@link NewUserRequest} в объект {@link User}.
     *
     * @param userRequest Объект {@link NewUserRequest}
     * @return Объект {@link User}
     */
    public static User toUser(NewUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }

    /**
     * Преобразует объект {@link User} в объект {@link UserDto}.
     *
     * @param user Объект {@link User}
     * @return Объект {@link UserDto}
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
