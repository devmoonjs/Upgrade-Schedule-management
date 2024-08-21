package com.sparta.upgradeschedulemanagement.entity;


import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String author;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Todo(TodoRequestDto requestDto) {
        this.author = requestDto.getAuthor();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeAuthor(String author) {
        this.author = author;
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

    @OneToMany(mappedBy = "todo")
    private List<Comment> commentList = new ArrayList<>();
}
