package ru.practicum.mainservice.controller.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.CategoryDto;
import ru.practicum.mainservice.dto.NewCategoryDto;
import ru.practicum.mainservice.mapper.CategoryMapper;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.service.CategoryService;

import javax.validation.Valid;

/**
 * Контроллер для обработки запросов от админа по категориям.
 */
@RestController()
@RequestMapping(path = "${main-service.admin.path}/categories")
@RequiredArgsConstructor
@Validated
public class CategoryControllerAdmin {
    /**
     * Сервис категорий.
     */
    private final CategoryService categoryService;

    /**
     * Обрабатывает HTTP-запрос POST для создания новой категории.
     *
     * @param newCategoryDto Данные новой категории.
     * @return Ответ с созданной категорией и статусом 201 Created.
     */
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        CategoryDto result = CategoryMapper.toCategoryDto(categoryService.addCategory(category));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Обрабатывает HTTP-запрос PATCH для обновления существующей категории.
     *
     * @param categoryDto Данные для обновления категории.
     * @param categoryId  Идентификатор категории, которую нужно обновить.
     * @return Ответ с обновленной категорией и статусом 200 OK.
     */
    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto,
                                                      @PathVariable("catId") Long categoryId) {
        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(categoryId);
        CategoryDto result = CategoryMapper.toCategoryDto(categoryService.updateCategory(category));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Обрабатывает HTTP-запрос DELETE для удаления категории по ее идентификатору.
     *
     * @param categoryId Идентификатор категории, которую нужно удалить.
     * @return Ответ со статусом 204 No Content.
     */
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("catId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
