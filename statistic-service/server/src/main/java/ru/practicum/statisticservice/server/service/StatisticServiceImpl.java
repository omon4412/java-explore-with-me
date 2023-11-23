package ru.practicum.statisticservice.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statisticservice.server.model.EndpointHit;
import ru.practicum.statisticservice.server.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository repository;

    @Override
    public EndpointHit hit(EndpointHit hit) {
        hit.setTimestamp(LocalDateTime.now());
        return repository.save(hit);
    }

    @Override
    public Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                             List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new RuntimeException("Дата");
        }
        if (uris == null) {
            return repository.getStatsForAll(start, end);
        }
        if (unique) {
            return repository.getStatsUnique(start, end, uris);
        }
        return repository.getStats(start, end, uris);
    }
}
