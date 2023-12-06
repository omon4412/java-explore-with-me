package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Jpa репозиторий событий.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndState(long id, EventState state);

    @Query("SELECT e FROM Event e WHERE " +
            "((:users) IS NULL OR e.initiator.id IN (:users)) " +
            "AND ((:states) IS NULL OR e.state IN (:states)) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND (cast((:rangeStart) as timestamp) IS NULL OR e.eventDate >= (:rangeStart)) " +
            "AND (cast((:rangeEnd) as timestamp) IS NULL OR e.eventDate <= (:rangeEnd))")
    Page<Event> searchEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e WHERE " +
            "((LOWER(e.annotation) LIKE COALESCE(:text, '%')) OR (LOWER(e.description) LIKE COALESCE(:text, '%'))) " +
            "AND (e.state='PUBLISHED') " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND ((:paid) IS NULL OR e.paid = (:paid)) " +
            "AND (cast((:rangeStart) as timestamp) IS NULL OR e.eventDate >= (:rangeStart)) " +
            "AND (cast((:rangeEnd) as timestamp) IS NULL OR e.eventDate <= (:rangeEnd)) ")
    Page<Event> searchEventsPublic(String text,
                                   List<Long> categories,
                                   Boolean paid,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);
}
