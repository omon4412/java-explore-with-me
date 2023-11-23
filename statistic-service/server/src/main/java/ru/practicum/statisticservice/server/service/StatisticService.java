package ru.practicum.statisticservice.server.service;

import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statisticservice.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для обработки статистики.
 */
public interface StatisticService {

    /**
     * Обработка нового обращения и сохранение его в базе данных.
     *
     * @param hit Новое обращение для сохранения.
     * @return Объект EndpointHit после сохранения.
     */
    EndpointHit hit(EndpointHit hit);

    /**
     * Получение статистики по обращениям в указанный временной период для заданных URI.
     *
     * @param start  Начальная временная метка.
     * @param end    Конечная временная метка.
     * @param uris   Список URI для фильтрации статистики.
     * @param unique Флаг, указывающий на необходимость статистики по уникальным IP.
     * @return Коллекция DTO объектов, представляющих статистику по обращениям.
     */
    Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
