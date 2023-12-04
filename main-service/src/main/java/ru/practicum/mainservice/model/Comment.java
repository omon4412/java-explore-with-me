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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    protected Event event;

    @ManyToOne
    @JoinColumn(name = "author_id")
    protected User author;

    @Column(name = "text")
    protected String text;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    protected Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("parentComment")
    protected Collection<Comment> childComments;

    @Column(name = "comment_date")
    protected LocalDateTime commentDate;
    @Column(name = "update_date")
    protected LocalDateTime updateDate;
}
