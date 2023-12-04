package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Compilation;

/**
 * Jpa репозиторий подборок.
 */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);
}
