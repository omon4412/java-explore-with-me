package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Модель пользователя.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "username")
    protected String username;

    /**
     * Почта пользователя.
     */
    @Column(name = "email")
    protected String email;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password_hash")
    protected String password;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Collection<Role> roles;
}
