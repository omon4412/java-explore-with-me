package ru.practicum.mainservice.model;

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
    CANCELED
}
