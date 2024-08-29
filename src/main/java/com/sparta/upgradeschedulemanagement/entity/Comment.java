package com.sparta.upgradeschedulemanagement.entity;

import com.sparta.upgradeschedulemanagement.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public Comment(CommentRequestDto requestDto, User user, Todo todo) {
        this.user = user;
        this.content = requestDto.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.todo = todo;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
