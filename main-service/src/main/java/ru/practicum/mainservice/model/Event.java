package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель события.
 */
@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    /**
     * Идентификатор события.
     */
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    /**
     * Категория события.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    protected Category category;

    /**
     * Аннотация события.
     */
    @Column(name = "annotation")
    protected String annotation;

    /**
     * Дата и время создания события.
     */
    @Column(name = "created_on")
    protected LocalDateTime createdOn;

    /**
     * Описание события.
     */
    @Column(name = "description")
    protected String description;

    /**
     * Дата и время проведения события.
     */
    @Column(name = "event_date")
    protected LocalDateTime eventDate;

    /**
     * Инициатор события.
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    protected User initiator;

    /**
     * Местоположение события.
     */
    @Column(name = "location")
    @Embedded
    protected Location location;

    /**
     * Флаг оплаты события.
     */
    @Column(name = "paid")
    protected boolean paid;

    /**
     * Ограничение по количеству участников события.
     */
    @Column(name = "participant_limit")
    protected long participantLimit;

    /**
     * Дата и время публикации события.
     */
    @Column(name = "published_on")
    protected LocalDateTime publishedOn;

    /**
     * Необходимость модерации заявок на участие в событии.
     */
    @Column(name = "request_moderation")
    protected boolean requestModeration;

    /**
     * Состояние события.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    protected EventState state;

    /**
     * Заголовок события.
     */
    @Column(name = "title")
    protected String title;
}
