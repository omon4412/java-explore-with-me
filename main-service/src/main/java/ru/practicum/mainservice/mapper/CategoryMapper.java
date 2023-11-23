package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.CategoryDto;
import ru.practicum.mainservice.dto.NewCategoryDto;
import ru.practicum.mainservice.model.Category;

/**
 * Маппер для преобразования объектов {@link Category}, {@link CategoryDto} и {@link NewCategoryDto}.
 */
public class CategoryMapper {

    private CategoryMapper() {
    }

    /**
     * Преобразует объект {@link CategoryDto} в объект {@link Category}.
     *
     * @param categoryDto Объект {@link CategoryDto}
     * @return Объект {@link Category}
     */
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    /**
     * Преобразует объект {@link NewCategoryDto} в объект {@link Category}.
     *
     * @param newCategoryDto Объект {@link NewCategoryDto}
     * @return Объект {@link Category}
     */
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    /**
     * Преобразует объект {@link Category} в объект {@link CategoryDto}.
     *
     * @param category Объект {@link Category}
     * @return Объект {@link CategoryDto}
     */
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
