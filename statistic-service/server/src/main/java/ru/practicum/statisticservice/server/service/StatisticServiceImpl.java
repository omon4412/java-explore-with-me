package ru.practicum.statisticservice.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statisticservice.server.exception.BadRequestException;
import ru.practicum.statisticservice.server.model.EndpointHit;
import ru.practicum.statisticservice.server.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository repository;

    @Override
    @Transactional
    public EndpointHit hit(EndpointHit hit) {
        hit.setTimestamp(LocalDateTime.now());
        return repository.save(hit);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                             List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Даты не должны пересекаться");
        }
        if (uris == null) {
            return repository.getStatsForAll(start, end);
        }
        List<String> modifiedUris = uris.stream()
                .map(uri -> uri.replaceAll("[\\[\\]]", ""))
                .collect(Collectors.toList());
        if (unique) {
            return repository.getStatsUnique(start, end, modifiedUris);
        }

        return repository.getStats(start, end, modifiedUris);
    }
}
