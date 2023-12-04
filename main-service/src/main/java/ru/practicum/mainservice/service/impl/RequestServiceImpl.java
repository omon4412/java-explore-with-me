package ru.practicum.mainservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.*;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.RequestService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public ParticipationRequestDto addRequest(long userId, long eventId) {
        User user = getOptionalUser(userId).get();
        Event event = getOptionalEvent(eventId).get();
        LocalDateTime now = LocalDateTime.now();
        Optional<Request> requestOptional = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (requestOptional.isPresent()) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId() == userId) {
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
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        getOptionalUser(userId).get();
        Request request = getOptionalRequest(requestId).get();
        request.setStatus(RequestStatus.CANCELED);
        Request result = requestRepository.save(request);
        return RequestMapper.toParticipationRequestDto(result);
    }

    @Override
    public Collection<ParticipationRequestDto> getAllByUserRequests(long userId) {
        Collection<Request> result = requestRepository.findAllByRequesterId(userId);
        return result.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private Optional<User> getOptionalUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", userId));
        }
        return userOptional;
    }

    private Optional<Event> getOptionalEvent(long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(String.format("Событие с ID=%d не найдено", eventId));
        }
        return eventOptional;
    }

    private Optional<Request> getOptionalRequest(long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            throw new NotFoundException(String.format("Запрос с ID=%d не найден", requestId));
        }
        return requestOptional;
    }
}
