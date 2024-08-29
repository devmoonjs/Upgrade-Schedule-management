package com.sparta.upgradeschedulemanagement.entity;

import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Todo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private Long userId;
    private String title;
    private String content;
    private String weather;

    public Todo(TodoRequestDto requestDto, String weather) {
        this.userId = requestDto.getUserId();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.weather = weather;
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

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<UserTodo> userTodoList = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();
}
