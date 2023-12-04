package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Request;
import ru.practicum.mainservice.model.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Jpa репозиторий запросов на участие в событии.
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByRequesterIdAndEventId(long requesterId, long eventId);

    Collection<Request> findAllByRequesterId(Long requesterId);

    Collection<Request> findAllByEventId(long eventId);

    long countAllByEventIdAndStatusIs(long eventId, RequestStatus status);

    Collection<Request> findAllByIdInAndStatus(Collection<Long> id, RequestStatus status);

    Collection<Request> findAllByIdIn(List<Long> requestIds);
}
