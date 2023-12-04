package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель для представления запросов на участие в событиях.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class Request {

    /**
     * Уникальный идентификатор запроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    /**
     * Связь с событием, к которому относится запрос.
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Связь с пользователем, отправившим запрос.
     */
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Статус запроса на участие в событии.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
}
