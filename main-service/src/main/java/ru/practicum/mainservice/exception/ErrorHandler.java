package ru.practicum.mainservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
     * Обработчик исключения {@link NotFoundException}.
     * Возникает, когда искомый объект не найден.
     *
     * @param ex Исключение {@link NotFoundException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final NotFoundException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Запрашиваемый объект не найден")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

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
     * Обработчик исключения {@link ConflictException}.
     * Возникает, когда происходит конфликт в данных.
     *
     * @param ex Исключение {@link ConflictException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Конфликт в данных")
                .status(HttpStatus.CONFLICT.name())
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
     * Обработчик исключения {@link DataIntegrityViolationException}.
     * Возникает, когда нарушается целостность данных (например, уникальное ограничение).
     *
     * @param ex Исключение {@link DataIntegrityViolationException}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message(ex.getMessage())
                .reason("Ошибка целостности данных")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link MissingServletRequestParameterException}.
     * Возникает, когда отсутствует обязательный параметр.
     *
     * @param ex Исключение {@link MissingServletRequestParameterException}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(MissingServletRequestParameterException ex) {
        return ApiError.builder()
                .errors(ex.getStackTrace())
                .message("Отсутствует обязательный параметр -" + ex.getParameterName())
                .reason("Отсутствует обязательный параметр")
                .status(HttpStatus.CONFLICT.toString())
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
