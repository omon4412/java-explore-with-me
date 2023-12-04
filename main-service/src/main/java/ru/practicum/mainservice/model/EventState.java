package ru.practicum.mainservice.model;

import java.util.Arrays;

/**
 * Перечисление, представляющее состояния события.
 */
public enum EventState {
    /**
     * Событие ожидает публикации.
     */
    PENDING,

    /**
     * Событие опубликовано.
     */
    PUBLISHED,

    /**
     * Событие отменено.
     */
    CANCELED;

    public static boolean contains(String statusName) {
        return Arrays.stream(EventState.values())
                .anyMatch(c -> statusName.equalsIgnoreCase(c.name()));
    }
}

