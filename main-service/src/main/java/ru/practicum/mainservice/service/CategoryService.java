package ru.practicum.mainservice.service;

import ru.practicum.mainservice.model.Category;

import java.util.Collection;

/**
 * Сервис для управления категориями.
 */
public interface CategoryService {

    /**
     * Добавляет новую категорию.
     *
     * @param category Новая категория.
     * @return Добавленная категория.
     */
    Category addCategory(Category category);

    /**
     * Обновляет информацию о существующей категории.
     *
     * @param category Категория с обновленными данными.
     * @return Обновленная категория.
     */
    Category updateCategory(Category category);

    /**
     * Удаляет категорию по её идентификатору.
     *
     * @param categoryId Идентификатор категории.
     */
    void deleteCategory(long categoryId);

    /**
     * Получает категорию по её идентификатору.
     *
     * @param categoryId Идентификатор категории.
     * @return Категория с заданным идентификатором.
     */
    Category getCategory(long categoryId);

    /**
     * Получает коллекцию категорий с применением пагинации.
     *
     * @param from Начальный индекс для пагинации.
     * @param size Количество элементов на странице.
     * @return Коллекция категорий с учетом пагинации.
     */
    Collection<Category> getCategories(Integer from, Integer size);
}