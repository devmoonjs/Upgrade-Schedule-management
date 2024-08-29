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

    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public Comment(CommentRequestDto requestDto, Todo todo) {
        this.userId = requestDto.getUserId();
        this.content = requestDto.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.todo = todo;
    }

    public void changeUser(Long userId) {
        this.userId = userId;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
