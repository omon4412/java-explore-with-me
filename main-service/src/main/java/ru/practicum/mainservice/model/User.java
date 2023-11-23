package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Модель пользователя.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "name")
    protected String name;

    /**
     * Почта пользователя.
     */
    @Column(name = "email")
    protected String email;
}
