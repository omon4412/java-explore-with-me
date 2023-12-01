package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.UpdateEventUserRequest;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.model.SortTypes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для описания бизнес-логики, связанной с событиями.
 */
public interface EventService {

    /**
     * Добавляет новое событие.
     *
     * @param eventDto Данные для нового события.
     * @param userId   Идентификатор пользователя-инициатора.
     * @return Полная информация о добавленном событии.
     */
    FullEventDto addEvent(NewEventDto eventDto, long userId);

    /**
     * Получает все события пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Индекс начального элемента для пагинации.
     * @param size   Размер страницы для пагинации.
     * @return Коллекция краткой информации о событиях пользователя.
     */
    Collection<EventShortDto> getUsersAllEvent(long userId, Integer from, Integer size);

    /**
     * Получает полную информацию о событии пользователя по его идентификатору.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Полная информация о событии пользователя.
     */
    FullEventDto getUserEventById(long userId, long eventId);

    /**
     * Обновляет информацию о событии пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @param request Запрос на обновление информации о событии.
     * @return Полная информация об обновленном событии пользователя.
     */
    FullEventDto updateUsersEventById(long userId, long eventId, UpdateEventUserRequest request);

    /**
     * Получает запросы на участие в событии пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Коллекция запросов на участие в событии пользователя.
     */
    Collection<ParticipationRequestDto> getUsersEventRequests(long userId, long eventId);

    /**
     * Изменяет статус запросов на участие в событии.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @param request Запрос на изменение статуса запросов.
     * @return Результат изменения статуса запросов на участие.
     */
    EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId,
                                                        EventRequestStatusUpdateRequest request);

    /**
     * Обновляет событие администратором.
     *
     * @param eventId Идентификатор события.
     * @param request Запрос на обновление события.
     * @return Полная информация об обновленном событии.
     */
    FullEventDto updateEventByAdmin(long eventId, UpdateEventUserRequest request);

    /**
     * Поиск событий с применением различных фильтров.
     *
     * @param users      Список идентификаторов пользователей для фильтрации.
     * @param states     Список состояний событий для фильтрации.
     * @param categories Список идентификаторов категорий для фильтрации.
     * @param rangeStart Начало временного интервала для фильтрации.
     * @param rangeEnd   Конец временного интервала для фильтрации.
     * @param from       Индекс начального элемента для пагинации.
     * @param size       Размер страницы для пагинации.
     * @return Коллекция полной информации о найденных событиях.
     */
    Collection<FullEventDto> searchEvents(List<Long> users, List<EventState> states,
                                          List<Long> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, int from, int size);

    /**
     * Получает полную информацию о событии для общедоступного доступа.
     *
     * @param eventId Идентификатор события.
     * @param request HTTP-запрос для получения информации о клиенте.
     * @return Полная информация о событии для общедоступного доступа.
     */
    FullEventDto getEventPublic(long eventId, HttpServletRequest request);

    /**
     * Поиск общедоступных событий с учетом различных критериев.
     *
     * @param text          Текст для поиска по событиям.
     * @param categories    Список идентификаторов категорий для фильтрации событий.
     * @param paid          Флаг указывающий, является ли событие платным.
     * @param rangeStart    Начало временного интервала для фильтрации.
     * @param rangeEnd      Конец временного интервала для фильтрации.
     * @param onlyAvailable Флаг указывающий, выводить только доступные события.
     * @param sort          Тип сортировки для вывода событий.
     * @param from          Индекс начального элемента для пагинации.
     * @param size          Размер страницы для пагинации.
     * @param request       HTTP-запрос для получения информации о клиенте.
     * @return Коллекция краткой информации об общедоступных событиях.
     */
    Collection<EventShortDto> searchEventsPublic(String text, List<Long> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable, SortTypes sort, int from, int size,
                                                 HttpServletRequest request);
}
