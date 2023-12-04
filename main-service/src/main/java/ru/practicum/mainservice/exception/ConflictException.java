package ru.practicum.mainservice.exception;

/**
 * Исключение, выбрасываемое, когда нет доступа.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
