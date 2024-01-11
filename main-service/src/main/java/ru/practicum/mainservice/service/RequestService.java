package ru.practicum.mainservice.service;

import org.springframework.security.core.Authentication;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;

import java.util.Collection;

/**
 * Интерфейс для описания бизнес-логики, связанной с запросами на участие в событиях.
 */
public interface RequestService {

    /**
     * Добавляет новый запрос на участие в событии.
     *
     * @param userId  Идентификатор пользователя, отправившего запрос.
     * @param eventId Идентификатор события, для которого отправлен запрос.
     * @return Информация о добавленном запросе на участие.
     */
    ParticipationRequestDto addRequest(Authentication userId, long eventId);

    /**
     * Отменяет запрос на участие в событии.
     *
     * @param userId    Идентификатор пользователя, отправившего запрос.
     * @param requestId Идентификатор запроса на участие, который нужно отменить.
     * @return Информация об отмененном запросе на участие.
     */
    ParticipationRequestDto cancelRequest(Authentication userId, long requestId);

    /**
     * Получает все запросы на участие пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Коллекция информации о запросах на участие пользователя.
     */
    Collection<ParticipationRequestDto> getAllByUserRequests(Authentication userId);
}
