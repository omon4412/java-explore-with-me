package ru.practicum.mainservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.dto.user.UserInfo;
import ru.practicum.mainservice.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
public interface UserService extends UserDetailsService {

    /**
     * Добавляет нового пользователя.
     *
     * @param user Новый пользователь.
     * @return Добавленный пользователь.
     */
    UserDto addUser(NewUserRequest user);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     */
    void deleteUserById(long userId);

    /**
     * Получает коллекцию пользователей с применением пагинации и фильтра по идентификаторам.
     *
     * @param ids  Список идентификаторов.
     * @param from Начальный индекс для пагинации.
     * @param size Количество элементов на странице.
     * @return Коллекция пользователей с учетом пагинации.
     */
    Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size);

    Optional<User> findByUsername(String username);

    User createNewUser(NewUserRequest registrationUserDto);

    UserInfo getUserInfo(Long userId);
}