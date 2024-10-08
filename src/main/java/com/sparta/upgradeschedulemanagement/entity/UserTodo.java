package com.sparta.upgradeschedulemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class UserTodo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userTodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserTodo(Todo todo, User user) {
        this.todo = todo;
        this.user = user;
    }
}
