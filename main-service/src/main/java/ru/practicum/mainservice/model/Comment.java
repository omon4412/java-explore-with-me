package ru.practicum.mainservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Модель комментария.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    /**
     * Идентификатор комментария.
     */
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Событие, к которому относится комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    protected Event event;

    /**
     * Автор комментария.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    protected User author;

    /**
     * Текст комментария.
     */
    @Column(name = "text")
    protected String text;

    /**
     * Родительский комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    protected Comment parentComment;

    /**
     * Дочерние комментарии.
     */
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("parentComment")
    protected Collection<Comment> childComments;

    /**
     * Дата создания комментария.
     */
    @Column(name = "comment_date")
    protected LocalDateTime commentDate;

    /**
     * Дата последнего обновления комментария.
     */
    @Column(name = "update_date")
    protected LocalDateTime updateDate;

    /**
     * Лайки других пользователей.
     */
    @ManyToMany
    @JoinTable(
            name = "comments_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;
}
