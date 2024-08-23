package com.sparta.upgradeschedulemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class UserTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userTodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "user_id")
    private User user;
}
