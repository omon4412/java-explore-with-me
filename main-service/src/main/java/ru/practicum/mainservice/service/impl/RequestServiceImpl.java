package ru.practicum.mainservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.*;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.mainservice.service.RequestService;
import ru.practicum.mainservice.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final RequestRepository requestRepository;

    @Override
    public ParticipationRequestDto addRequest(Authentication authentication, long eventId) {
        User user = getOptionalUser(authentication).get();
        Event event = getOptionalEvent(eventId).get();
        LocalDateTime now = LocalDateTime.now();
        Optional<Request> requestOptional = requestRepository.findByRequesterIdAndEventId(user.getId(), eventId);
        if (requestOptional.isPresent()) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии ");
        }
        if (event.getParticipantLimit() != 0
                && requestRepository.countAllByEventIdAndStatusIs(eventId, RequestStatus.CONFIRMED)
                == event.getParticipantLimit()) {
            throw new ConflictException("На событие зарегистрировалось максимальное количество участников");
        }

        RequestStatus status;
        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            status = RequestStatus.PENDING;
        } else {
            status = RequestStatus.CONFIRMED;
        }

        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(now)
                .status(status)
                .build();

        Request result = requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(result);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Authentication authentication, long requestId) {
        getOptionalUser(authentication).get();
        Request request = getOptionalRequest(requestId).get();
        request.setStatus(RequestStatus.CANCELED);
        Request result = requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(result);
    }

    @Override
    public Collection<ParticipationRequestDto> getAllByUserRequests(Authentication authentication) {
        User user = getOptionalUser(authentication).get();
        Collection<Request> result = requestRepository.findAllByRequesterId(user.getId());
        return result.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает Optional<User> для заданного идентификатора пользователя.
     *
     * @param authentication Идентификатор пользователя.
     * @return Optional<User>, содержащий пользователя, если найден.
     * @throws NotFoundException Если пользователь с заданным идентификатором не найден.
     */
    private Optional<User> getOptionalUser(Authentication authentication) {
        Optional<User> userOptional = userService.findByUsername(authentication.getName());
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с ID=%s не найден", authentication.getName()));
        }
        return userOptional;
    }

    /**
     * Получает Optional<Event> для заданного идентификатора события.
     *
     * @param eventId Идентификатор события.
     * @return Optional<Event>, содержащий событие, если найдено.
     * @throws NotFoundException Если событие с заданным идентификатором не найдено.
     */
    private Optional<Event> getOptionalEvent(long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(String.format("Событие с ID=%d не найдено", eventId));
        }
        return eventOptional;
    }

    /**
     * Получает Optional<Request> для заданного идентификатора запроса.
     *
     * @param requestId Идентификатор запроса.
     * @return Optional<Request>, содержащий запрос, если найден.
     * @throws NotFoundException Если запрос с заданным идентификатором не найден.
     */
    private Optional<Request> getOptionalRequest(long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            throw new NotFoundException(String.format("Запрос с ID=%d не найден", requestId));
        }
        return requestOptional;
    }
}
