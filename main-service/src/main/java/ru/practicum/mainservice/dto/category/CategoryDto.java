package ru.practicum.mainservice.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления категории.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    /**
     * Идентификатор категории.
     */
    protected long id;

    /**
     * Наименование категории.
     */
    protected String name;
}
