package ru.practicum.mainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @NotBlank
    @Size(min = 1, max = 50)
    protected String name;
}
