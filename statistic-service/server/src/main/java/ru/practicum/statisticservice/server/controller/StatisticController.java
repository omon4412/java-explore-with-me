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

@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService service;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EndpointHitDto hit(@Validated @RequestBody EndpointHitDto hit, HttpServletRequest request) {
        hit.setIp(request.getRemoteAddr());
        return EndPointMapper.toEndPointHitDto(service.hit(EndPointMapper.toEndPointHit(hit)));
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(defaultValue = "false") boolean unique) {

        return service.getStats(start, end, uris, unique);
    }
}
