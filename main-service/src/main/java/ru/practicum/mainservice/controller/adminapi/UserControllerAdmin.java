package ru.practicum.mainservice.controller.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Контроллер для обработки запросов от админа по пользователям.
 */
@RestController()
@RequestMapping(path = "${main-service.admin.path}/users")
@RequiredArgsConstructor
public class UserControllerAdmin {
    /**
     * Сервис пользователей.
     */
    private final UserService userService;

    /**
     * Обрабатывает HTTP-запрос POST для создания нового пользователя.
     *
     * @param userRequest Данные нового пользователя.
     * @return Ответ с созданным пользователем и статусом 201 Created.
     */
    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody NewUserRequest userRequest) {
        User user = UserMapper.toUser(userRequest);
        UserDto result = UserMapper.toUserDto(userService.addUser(user));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Обрабатывает HTTP-запрос DELETE для удаления пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя, которого нужно удалить.
     * @return Ответ со статусом 204 No Content.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Обрабатывает HTTP-запрос GET для получения коллекции пользователей с пагинацией.
     *
     * @param ids  Список идентификаторов.
     * @param from Начальный индекс для пагинации.
     * @param size Количество элементов на странице.
     * @return Ответ с коллекцией пользователей и статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers(
            @RequestParam(required = false, defaultValue = "") Collection<Long> ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Collection<User> users = userService.getUsers(ids, from, size);
        Collection<UserDto> usersDto = users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
}
