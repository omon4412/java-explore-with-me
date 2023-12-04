package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Comment;

/**
 * Jpa репозиторий комментариев.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
