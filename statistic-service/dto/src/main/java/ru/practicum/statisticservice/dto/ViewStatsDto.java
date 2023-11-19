package ru.practicum.statisticservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект, представляющий статистику просмотров для конкретной конечной точки и приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    /**
     * Наименование приложения.
     */
    private String app;

    /**
     * Конечная точка, для которой предоставляется статистика.
     */
    private String uri;

    /**
     * Количество обращений к указанной конечной точки и приложения.
     */
    private Long hits;
}
