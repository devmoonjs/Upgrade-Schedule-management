package com.sparta.upgradeschedulemanagement.entity;


import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
