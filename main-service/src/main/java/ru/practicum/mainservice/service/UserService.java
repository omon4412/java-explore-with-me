package ru.practicum.mainservice.service;

import ru.practicum.mainservice.model.User;

import java.util.Collection;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {

    /**
     * Добавляет нового пользователя.
     *
     * @param user Новый пользователь.
     * @return Добавленный пользователь.
     */
    User addUser(User user);

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
}