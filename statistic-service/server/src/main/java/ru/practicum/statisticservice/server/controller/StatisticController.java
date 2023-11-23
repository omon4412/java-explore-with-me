package ru.practicum.statisticservice.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statisticservice.dto.EndpointHitDto;
import ru.practicum.statisticservice.dto.ViewStatsDto;
import ru.practicum.statisticservice.server.EndPointMapper;
import ru.practicum.statisticservice.server.service.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с сбором и предоставлением статистики.
 */
@RestController
@RequiredArgsConstructor
public class StatisticController {
    /**
     * Сервис статистики.
     */
    private final StatisticService service;

    /**
     * Обработчик POST-запроса для записи обращения на конечную точку.
     *
     * @param hit     DTO объект, представляющий обращение на конечную точку.
     * @param request Объект HttpServletRequest для получения IP-адреса клиента.
     * @return DTO объект, представляющий обращение на конечную точку после обработки сервисом.
     */
    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EndpointHitDto hit(@Validated @RequestBody EndpointHitDto hit, HttpServletRequest request) {
        hit.setIp(request.getRemoteAddr());
        return EndPointMapper.toEndPointHitDto(service.hit(EndPointMapper.toEndPointHit(hit)));
    }

    /**
     * Обработчик GET-запроса для получения статистики за определенный временной период.
     *
     * @param start  Начальная временная метка.
     * @param end    Конечная временная метка.
     * @param uris   Список конечных точек для фильтрации статистики.
     * @param unique Флаг, указывающий на необходимость учета уникальных посещений.
     * @return Коллекция DTO объектов, представляющих статистику за указанный период.
     */
    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(defaultValue = "false") boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
