package ru.practicum.statisticservice.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая обращение на конечную точку и хранящаяся в базе данных.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "STATISTICS")
public class EndpointHit {
    /**
     * Идентификатор обращения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование приложения, отправившего запрос.
     */
    @Column(nullable = false)
    private String app;

    /**
     * Конечная точка.
     */
    @Column(nullable = false)
    private String uri;

    /**
     * IP-адрес клиента.
     */
    @Column(nullable = false)
    private String ip;

    /**
     * Временная метка создания обращения.
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime timestamp;
}
