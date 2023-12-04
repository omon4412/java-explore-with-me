package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Category;

/**
 * Jpa репозиторий категорий.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
