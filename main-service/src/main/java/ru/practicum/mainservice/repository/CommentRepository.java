package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.model.Comment;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Jpa репозиторий комментариев.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findByEventIdAndParentCommentIsNull(long eventId);

    @Query("SELECT c FROM Comment c WHERE " +
            "LOWER(c.text) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND c.event.id = :eventId")
    Collection<Comment> findByEventIdAndText(@Param("eventId") long eventId, @Param("text") String text);

    @Query("SELECT c FROM Comment c WHERE " +
            "c.author.id = :userId " +
            "AND ((LOWER(c.text) LIKE COALESCE(:text, '%'))) " +
            "AND ((:events) IS NULL OR c.event.id IN (:events)) " +
            "AND (cast((:rangeStart) as timestamp) IS NULL OR c.commentDate >= (:rangeStart)) " +
            "AND (cast((:rangeEnd) as timestamp) IS NULL OR c.commentDate <= (:rangeEnd)) ")
    Page<Comment> getAllUserComments(Long userId, String text, List<Long> events, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Pageable pageable);

    long countAllByEventId(long eventId);

    @Query("SELECT c.likes.size FROM Comment c WHERE " +
            "c.id = :commentId")
    long countLikesByComment(Long commentId);
}
