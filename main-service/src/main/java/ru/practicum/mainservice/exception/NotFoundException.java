package ru.practicum.mainservice.exception;

/**
 * Исключение, выбрасываемое, когда объект не найден.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
