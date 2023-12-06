package ru.practicum.mainservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CompilationMapper;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.RequestStatus;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.mainservice.service.CompilationService;
import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statiticservice.client.StatisticClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatisticClient statisticClient;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() == null) {
            compilation.setEventsInCompilation(Collections.emptySet());
            return CompilationMapper.toCompilationDto(compilation);
        } else {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            return getCompilationDto(compilation, new HashSet<>(events));
        }
    }

    @Override
    public void deleteCompilationById(Long compId) {
        Compilation compilation = getOptionalCompilation(compId).get();
        compilationRepository.delete(compilation);
    }

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationRepository.findAll(pageRequest).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAllByPinnedIs(pinned, pageRequest).stream()
                .map(c -> getCompilationDto(c, new HashSet<>(c.getEventsInCompilation())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getOptionalCompilation(compId).get();
        Set<Event> events = compilation.getEventsInCompilation();
        return getCompilationDto(compilation, new HashSet<>(events));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationRequest) {
        Compilation compilation = getOptionalCompilation(compId).get();

        if (compilationRequest.getPinned() != null) {
            compilation.setPinned(compilationRequest.getPinned());
        }
        if (compilationRequest.getTitle() != null) {
            compilation.setTitle(compilationRequest.getTitle());
        }
        if (compilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationRequest.getEvents());
            return getCompilationDto(compilation, new HashSet<>(events));
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    /**
     * Получает DTO для подборки на основе объекта Compilation и множества связанных событий.
     *
     * @param compilation Объект Compilation.
     * @param events      Множество связанных событий.
     * @return CompilationDto, содержащий информацию о подборке и её событиях.
     * @throws NotFoundException Если подборка с заданным идентификатором не найдена.
     */
    private CompilationDto getCompilationDto(Compilation compilation, HashSet<Event> events) {
        compilation.setEventsInCompilation(events);
        List<String> uris = events.stream()
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
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        compilationDto.getEvents()
                .forEach(event -> {
                    if (viewStatsMap.containsKey("/events/" + event.getId())) {
                        event.setViews(viewStatsMap.get("/events/" + event.getId()).getHits());
                    } else {
                        event.setViews(0L);
                    }
                    long confirmedRequestsCount = requestRepository
                            .countAllByEventIdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(confirmedRequestsCount);
                });
        return compilationDto;
    }

    /**
     * Получает Optional<Compilation> для заданного идентификатора подборки.
     *
     * @param compId Идентификатор подборки.
     * @return Optional<Compilation>, содержащий подборку, если найдена.
     * @throws NotFoundException Если подборка с заданным идентификатором не найдена.
     */
    private Optional<Compilation> getOptionalCompilation(long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isEmpty()) {
            throw new NotFoundException(String.format("Подборка с ID=%d не найдена", compId));
        }
        return compilationOptional;
    }
}
