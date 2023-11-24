package ru.practicum.statisticservice.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

/**
 * Обработчик всех исключений.
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Обработчик исключения {@link BadRequestException}.
     * Возникает, когда приходят неверные данные.
     *
     * @param ex Исключение {@link BadRequestException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Неверные данные")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link ConstraintViolationException}.
     * Возникает, когда действие нарушает ограничение на структуру модели.
     *
     * @param ex Исключение {@link ConstraintViolationException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onConstraintValidationException(ConstraintViolationException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Нарушение ограничений на структуру модели")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link MethodArgumentNotValidException}.
     * Возникает когда проверка аргумента с аннотацией @Valid не удалась
     *
     * @param ex Исключение {@link MethodArgumentNotValidException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Нарушение ограничений на структуру модели")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link HttpRequestMethodNotSupportedException}.
     * Возникает когда обработчик запросов не поддерживает определенный метод запроса
     *
     * @param ex Исключение {@link HttpRequestMethodNotSupportedException}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Метод не поддерживается")
                .status(HttpStatus.METHOD_NOT_ALLOWED.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик всевозможных исключений во время работы программы.
     *
     * @param ex Исключение {@link Throwable}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable ex) {
        ex.printStackTrace();
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Внутрення ошибка сервера")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
