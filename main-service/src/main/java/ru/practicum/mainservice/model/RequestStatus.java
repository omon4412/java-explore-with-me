package ru.practicum.mainservice.model;

/**
 * Перечисление, представляющее возможные статусы запросов на участие в событиях.
 */
public enum RequestStatus {
    /**
     * Запрос ожидает рассмотрения.
     */
    PENDING,

    /**
     * Запрос подтвержден.
     */
    CONFIRMED,

    /**
     * Запрос отклонен.
     */
    REJECTED,

    /**
     * Запрос отменен.
     */
    CANCELED
}

