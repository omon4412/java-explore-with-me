package ru.practicum.mainservice.model;

import java.util.Arrays;

/**
 * Перечисление, представляющее возможные типы сортировки событий.
 */
public enum SortTypes {
    /**
     * Сортировка по дате события.
     */
    EVENT_DATE,

    /**
     * Сортировка по количеству просмотров.
     */
    VIEWS;

    /**
     * Проверяет, содержится ли указанный тип сортировки в перечислении.
     *
     * @param sortType Тип сортировки для проверки.
     * @return true, если тип сортировки существует в перечислении, в противном случае - false.
     */
    public static boolean contains(String sortType) {
        return Arrays.stream(SortTypes.values())
                .anyMatch(c -> sortType.equalsIgnoreCase(c.name()));
    }
}

