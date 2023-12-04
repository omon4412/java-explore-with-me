package ru.practicum.mainservice.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос на обновление информации о событии администратором.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventAdminRequest extends UpdateEventUserRequest {
}
