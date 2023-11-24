package ru.practicum.mainservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.event.FullEventDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.EventState;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.UserRepository;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.statiticservice.client.StatisticClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticClient statisticClient;

    @Override
    public FullEventDto addEvent(NewEventDto eventDto, long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", userId));
        }
        Optional<Category> categoryOptional = categoryRepository.findById(eventDto.getCategory());
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException(String.format("Категория с ID=%d не найдена", eventDto.getCategory()));
        }
        LocalDateTime now = LocalDateTime.now();
        if (!eventDto.getEventDate().isAfter(now.plusHours(2))) {
            throw new BadRequestException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
        Event event = EventMapper.toEvent(eventDto);

        event.setCreatedOn(now);
        if (!event.isRequestModeration()) {
            event.setPublishedOn(now);
            event.setState(EventState.PUBLISHED);
        } else {
            event.setState(EventState.PENDING);
        }

        event.setInitiator(userOptional.get());
        event.setCategory(categoryOptional.get());

        FullEventDto fullEventDto = EventMapper.toFullEventDto(eventRepository.save(event));
        fullEventDto.setViews(0L);
        fullEventDto.setConfirmedRequests(0L);

        return fullEventDto;
    }

    //        EndpointHitDto endpointHitDto = new EndpointHitDto("test", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
//        statisticClient.addStatistic(endpointHitDto);
}
