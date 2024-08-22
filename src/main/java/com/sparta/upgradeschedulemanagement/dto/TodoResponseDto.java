package com.sparta.upgradeschedulemanagement.dto;

import com.sparta.upgradeschedulemanagement.entity.Todo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class TodoResponseDto {
    private final Long todoId;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TodoResponseDto of(Todo todo) {
        return new TodoResponseDto(
                todo.getTodoId(),
                todo.getUserId(),
                todo.getTitle(),
                todo.getContent(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
