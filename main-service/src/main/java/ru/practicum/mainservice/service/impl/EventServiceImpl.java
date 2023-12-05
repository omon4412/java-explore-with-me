package ru.practicum.mainservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.UpdateEventUserRequest;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CommentMapper;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.*;
import ru.practicum.mainservice.repository.*;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.statisticservice.dto.EndpointHitDto;
import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statiticservice.client.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticClient statisticClient;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    @Transactional
    public FullEventDto addEvent(NewEventDto eventDto, long userId) {
        Optional<User> userOptional = getOptionalUser(userId);
        Optional<Category> categoryOptional = getOptionalCategory(eventDto.getCategory());

        LocalDateTime now = LocalDateTime.now();
        if (!eventDto.getEventDate().isAfter(now.plusHours(2))) {
            throw new BadRequestException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
        Event event = EventMapper.toEvent(eventDto);

        event.setCreatedOn(now);

        event.setState(EventState.PENDING);

        event.setInitiator(userOptional.get());
        event.setCategory(categoryOptional.get());

        FullEventDto fullEventDto = EventMapper.toFullEventDto(eventRepository.save(event));
        fullEventDto.setViews(0L);
        fullEventDto.setConfirmedRequests(0L);

        return fullEventDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventShortDto> getUsersAllEvent(long userId, Integer from, Integer size) {
        getOptionalUser(userId);

        Collection<Event> eventCollection = eventRepository.findAllByInitiatorId(userId,
                PageRequest.of(from / size, size)).getContent();

        List<String> uris = eventCollection.stream()
                .map(id -> "/events/" + id.getId())
                .collect(Collectors.toList());

        ResponseEntity<Object> response = statisticClient.getStatistic(
                LocalDateTime.of(1980, 1, 1, 0, 0, 0),
                LocalDateTime.now(),
                uris, true);

        Map<String, ViewStatsDto> viewStatsMap = new HashMap<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<ViewStatsDto> viewStatsList = objectMapper.convertValue(responseBody, new TypeReference<>() {
                });
                if (!viewStatsList.isEmpty()) {
                    viewStatsList.forEach(h -> viewStatsMap.put(h.getUri(), h));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return eventCollection.stream()
                .map(EventMapper::toEventShortDto)
                .peek(event -> {
                    if (viewStatsMap.containsKey("/events/" + event.getId())) {
                        event.setViews(viewStatsMap.get("/events/" + event.getId()).getHits());
                    } else {
                        event.setViews(0L);
                    }
                    long confirmedRequestsCount = requestRepository
                            .countAllByEventIdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(confirmedRequestsCount);
                    long commentsCount = commentRepository.countAllByEventId(event.getId());
                    event.setCommentsCount(commentsCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDto getUserEventById(long userId, long eventId) {
        getOptionalUser(userId);
        Event event = getOptionalEvent(eventId).get();
        FullEventDto fullEventDto = EventMapper.toFullEventDto(event);
        long hitCount = getHitCount(eventId);
        fullEventDto.setViews(hitCount);
        long confirmedRequestsCount = requestRepository.countAllByEventIdAndStatusIs(eventId,
                RequestStatus.CONFIRMED);
        fullEventDto.setConfirmedRequests(confirmedRequestsCount);
        return fullEventDto;
    }

    @Override
    @Transactional
    public FullEventDto updateUsersEventById(long userId, long eventId, UpdateEventUserRequest request) {
        getOptionalUser(userId);
        Event event = getOptionalEvent(eventId).get();
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Это событие другого пользователя");
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя обновить опубликованное событие");
        }
        LocalDateTime now = LocalDateTime.now();
        if (request.getEventDate() != null) {
            if (request.getEventDate().isAfter(now.plusHours(2))) {
                throw new BadRequestException("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента");
            }
            event.setEventDate(request.getEventDate());
        }
        checkEventForUpdate(request, event);
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        Event updatedEvent = eventRepository.save(event);
        return getFullEventDto(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ParticipationRequestDto> getUsersEventRequests(long userId, long eventId) {
        getOptionalUser(userId);
        getOptionalEvent(eventId);
        Collection<Request> allByEventId = requestRepository.findAllByEventId(eventId);

        return allByEventId.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId,
                                                               EventRequestStatusUpdateRequest request) {
        getOptionalUser(userId);
        Event event = getOptionalEvent(eventId).get();
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Это событие не требует подтверждения запросов");
        }
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Это событие другого пользователя");
        }

        List<Long> requestIds = request.getRequestIds();
        RequestStatus requestStatus = request.getStatus();

        Collection<Request> repositoryAllById = requestRepository.findAllByIdIn(requestIds);

        if (requestStatus == RequestStatus.CONFIRMED) {
            long confirmedRequestsCount = requestRepository
                    .countAllByEventIdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);

            if (confirmedRequestsCount + requestIds.size() > event.getParticipantLimit()) {
                throw new ConflictException("На событие зарегистрировалось максимальное количество участников");
            }

            repositoryAllById.forEach(s -> {
                if (s.getStatus() == RequestStatus.CONFIRMED) {
                    throw new ConflictException("Попытка принять уже принятую заявку на участие в событии");
                }
                s.setStatus(requestStatus);
            });
            requestRepository.saveAll(repositoryAllById);

            EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
            result.setConfirmedRequests(repositoryAllById.stream()
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList()));
            result.setRejectedRequests(Collections.emptyList());
            return result;
        } else if (requestStatus == RequestStatus.REJECTED) {
            repositoryAllById.forEach(s -> {
                if (s.getStatus() == RequestStatus.CONFIRMED) {
                    throw new ConflictException("Попытка отменить уже принятую заявку на участие в событии");
                }
                s.setStatus(requestStatus);
            });
            requestRepository.saveAll(repositoryAllById);

            EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
            result.setRejectedRequests(repositoryAllById.stream()
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList()));
            result.setConfirmedRequests(Collections.emptyList());
            return result;
        } else {
            throw new BadRequestException("Неверный статус");
        }
    }

    @Override
    @Transactional
    public FullEventDto updateEventByAdmin(long eventId, UpdateEventUserRequest request) {
        Event event = getOptionalEvent(eventId).get();
        if (event.getPublishedOn() != null) {
            LocalDateTime minEventDate = event.getPublishedOn().plusHours(1);

            if (request.getEventDate() != null) {
                if (event.getEventDate().isBefore(minEventDate)) {
                    throw new ConflictException("Дата начала события должна быть не ранее " +
                            "чем за час от даты публикации.");
                } else {
                    event.setEventDate(request.getEventDate());
                }
            }
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        checkEventForUpdate(request, event);
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState() == EventState.PENDING) {
                        event.setState(EventState.PUBLISHED);
                    } else {
                        throw new ConflictException("Событие можно публиковать, " +
                                "только если оно в состоянии ожидания публикации");
                    }
                    break;
                case REJECT_EVENT:
                    if (event.getState() != EventState.PUBLISHED) {
                        event.setState(EventState.CANCELED);
                    } else {
                        throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано");
                    }
                    break;
                default:
                    throw new BadRequestException("Недопустимое состояние");
            }
        }
        Event updatedEvent = eventRepository.save(event);
        return getFullEventDto(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<FullEventDto> searchEvents(List<Long> users, List<EventState> states,
                                                 List<Long> categories, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        log.info("searchEvents: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, pageable={}",
                users, states, categories, rangeStart, rangeEnd, pageable);
        Collection<Event> events = eventRepository.searchEvents(users, states, categories,
                rangeStart, rangeEnd, pageable).getContent();
        return events.stream()
                .map(this::getFullEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDto getEventPublic(long eventId, HttpServletRequest request) {
        addStatistics(request);
        Event event = getOptionalEvent(eventId).get();
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(String.format("Событие с ID=%d не найдено", eventId));
        }
        return getFullEventDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventShortDto> searchEventsPublic(String text, List<Long> categories,
                                                        Boolean paid, LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                        SortTypes sort, int from, int size,
                                                        HttpServletRequest request) {

        Pageable pageable = PageRequest.of(from / size, size);
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException("Дата окончания не может быть раньше даты начала");
            }
        } else {
            rangeStart = LocalDateTime.now();
        }
        if (text != null) {
            text = "%" + text.toLowerCase() + "%";
        }
        addStatistics(request);
        Collection<Event> events = eventRepository.searchEventsPublic(text, categories, paid,
                rangeStart, rangeEnd, pageable).getContent();

        return events.stream()
                .filter(e -> {
                    if (onlyAvailable)
                        return requestRepository.countAllByEventIdAndStatusIs(e.getId(),
                                RequestStatus.CONFIRMED) < e.getParticipantLimit();
                    else return true;
                })
                .map(this::getShortEventDto)
                .sorted((event1, event2) -> {
                    if (SortTypes.EVENT_DATE.equals(sort)) {
                        return event2.getEventDate().compareTo(event1.getEventDate());
                    } else {
                        return Long.compare(event2.getViews(), event1.getViews());
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentDto> getEventComments(long eventId, String keyWord) {
        getOptionalEvent(eventId);
        Collection<Comment> comments;
        if (keyWord == null) {
            comments = commentRepository.findByEventId(eventId);
        } else {
            comments = commentRepository.findByEventIdAndText(eventId, keyWord);
        }

        return comments.stream()
                .map(c -> {
                    CommentDto commentDto = CommentMapper.toCommentDto(c);
                    commentDto.setIsEventAuthor(c.getAuthor().getId().equals(c.getEvent().getInitiator().getId()));
                    return commentDto;
                })
                .collect(Collectors.toList());
    }

    private FullEventDto getFullEventDto(Event event) {
        FullEventDto fullEventDto = EventMapper.toFullEventDto(event);
        long confirmedRequestsCount = requestRepository.countAllByEventIdAndStatusIs(event.getId(),
                RequestStatus.CONFIRMED);
        fullEventDto.setConfirmedRequests(confirmedRequestsCount);
        long hitCount = getHitCount(event.getId());
        fullEventDto.setViews(hitCount);
        Collection<Comment> comments = commentRepository.findByEventId(event.getId());
        fullEventDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return fullEventDto;
    }

    private EventShortDto getShortEventDto(Event event) {
        EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
        long confirmedRequestsCount = requestRepository.countAllByEventIdAndStatusIs(event.getId(),
                RequestStatus.CONFIRMED);
        eventShortDto.setConfirmedRequests(confirmedRequestsCount);
        long hitCount = getHitCount(event.getId());
        eventShortDto.setViews(hitCount);
        long commentsCount = commentRepository.countAllByEventId(event.getId());
        eventShortDto.setCommentsCount(commentsCount);
        return eventShortDto;
    }

    private void addStatistics(HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        ResponseEntity<Object> responseEntity = statisticClient.addStatistic(EndpointHitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(now)
                .build());
        if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
            throw new RuntimeException("Сервер статистики недоступен");
        }
    }

    private void checkEventForUpdate(UpdateEventUserRequest request, Event event) {
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getCategory() != null) {
            Optional<Category> categoryOptional = getOptionalCategory(request.getCategory());
            event.setCategory(categoryOptional.get());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
    }

    private long getHitCount(long eventId) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<Object> response = statisticClient.getStatistic(
                LocalDateTime.of(1980, 1, 1, 0, 0, 0), LocalDateTime.now(),
                List.of("/events/" + eventId), true);
        long hitCount = 0;
        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<ViewStatsDto> viewStatsList = objectMapper.convertValue(responseBody, new TypeReference<>() {
                });
                if (!viewStatsList.isEmpty()) {
                    hitCount = viewStatsList.get(0).getHits();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hitCount;
    }

    private Optional<Category> getOptionalCategory(long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException(String.format("Категория с ID=%d не найдена", categoryId));
        }
        return categoryOptional;
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
}
