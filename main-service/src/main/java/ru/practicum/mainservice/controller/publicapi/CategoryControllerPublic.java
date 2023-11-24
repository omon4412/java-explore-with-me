package ru.practicum.mainservice.controller.publicapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.service.CategoryService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Контроллер для обработки запросов по категориям.
 */
@RestController()
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryControllerPublic {
    /**
     * Сервис категорий.
     */
    private final CategoryService categoryService;

    /**
     * Обрабатывает HTTP-запрос GET для получения категории по ее идентификатору.
     *
     * @param categoryId Идентификатор категории, которую нужно получить.
     * @return Ответ со статусом 204 Ok.
     */
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("catId") Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Обрабатывает HTTP-запрос GET для получения коллекции категорий с пагинацией.
     *
     * @param from Начальный индекс для пагинации.
     * @param size Количество элементов на странице.
     * @return Ответ с коллекцией категорий и статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<Collection<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Collection<Category> categories = categoryService.getCategories(from, size);
        Collection<CategoryDto> categoriesDto = categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoriesDto, HttpStatus.OK);
    }
}
