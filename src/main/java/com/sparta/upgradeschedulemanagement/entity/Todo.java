package com.sparta.upgradeschedulemanagement.entity;


import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Setter
    private String weather;

    public Todo(TodoRequestDto requestDto) {
        this.userId = requestDto.getUserId();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeUserId(Long userId) {
        this.userId = userId;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changContent(String content) {
        this.content = content;
    }

    public void changeUpdateAt() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<UserTodo> userTodoList = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();
}
