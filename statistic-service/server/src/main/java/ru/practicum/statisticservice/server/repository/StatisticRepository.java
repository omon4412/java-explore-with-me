package ru.practicum.statisticservice.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statisticservice.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Репозиторий для работы с данными статистики в базе данных.
 */
@Repository
public interface StatisticRepository extends JpaRepository<EndpointHit, Long> {
    /**
     * Получение статистики по количеству обращений для заданных URI в указанный временной период.
     *
     * @param start Начальная временная метка.
     * @param end   Конечная временная метка.
     * @param uris  Список URI для фильтрации статистики.
     * @return Коллекция DTO объектов, представляющих статистику по количеству обращений.
     */
    @Query("SELECT new ru.practicum.statisticservice.dto.ViewStatsDto(s.app, s.uri, COUNT(s)) " +
            "FROM EndpointHit s WHERE (s.uri IN :uris) " +
            "AND s.timestamp BETWEEN :start AND :end GROUP BY s.app, s.uri ORDER BY COUNT(s) desc ")
    Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    /**
     * Получение статистики по количеству уникальных IP для заданных URI в указанный временной период.
     *
     * @param start Начальная временная метка.
     * @param end   Конечная временная метка.
     * @param uris  Список URI для фильтрации статистики.
     * @return Коллекция DTO объектов, представляющих статистику по количеству уникальных IP.
     */
    @Query("SELECT new ru.practicum.statisticservice.dto.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip))" +
            "FROM EndpointHit s WHERE (s.uri IN :uris) AND s.timestamp BETWEEN :start " +
            "AND :end GROUP BY s.app, s.uri ORDER BY COUNT(DISTINCT s.ip) desc")
    Collection<ViewStatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    /**
     * Получение статистики по количеству хитов для всех URI в указанный временной период.
     *
     * @param start Начальная временная метка.
     * @param end   Конечная временная метка.
     * @return Коллекция DTO объектов, представляющих общую статистику для всех URI.
     */
    @Query("SELECT new ru.practicum.statisticservice.dto.ViewStatsDto(s.app, s.uri, COUNT(s)) " +
            "FROM EndpointHit s WHERE s.timestamp BETWEEN :start AND :end GROUP BY s.app, s.uri ORDER BY COUNT(s) desc")
    Collection<ViewStatsDto> getStatsForAll(LocalDateTime start, LocalDateTime end);
}
