package ru.practicum.statisticservice.server.service;

import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statisticservice.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticService {
    EndpointHit hit(EndpointHit hit);

    Collection<ViewStatsDto> getStats(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris,
                                      boolean unique);
}
