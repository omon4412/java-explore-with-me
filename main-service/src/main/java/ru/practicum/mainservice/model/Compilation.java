package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель подборки.
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    /**
     * Идентификатор подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    protected Long id;

    /**
     * События {@link Event}, находящиеся в подборке.
     */
    @ManyToMany
    @JoinTable(name = "compilations_event", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> eventsInCompilation;

    /**
     * Флаг, закреплена подборка или нет.
     */
    @Column(name = "pinned")
    private Boolean pinned;

    /**
     * Заголовок подборки.
     */
    @Column(name = "title")
    private String title;
}
