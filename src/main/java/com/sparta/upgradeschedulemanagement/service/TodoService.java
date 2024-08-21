package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import com.sparta.upgradeschedulemanagement.dto.TodoResponseDto;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Transactional
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo = new Todo(requestDto);
        return TodoResponseDto.of(todoRepository.save(todo));
    }

    public TodoResponseDto getTodo(Long todoId) {
        return TodoResponseDto.of(findTodoById(todoId));
    }


    public TodoResponseDto updateTodo(Long todoId, TodoRequestDto requestDto) {
        Todo todo = findTodoById(todoId);
        if (requestDto.getAuthor() != null) todo.changeAuthor(requestDto.getAuthor());
        if (requestDto.getTitle() != null) todo.changeTitle(requestDto.getTitle());
        if (requestDto.getContent() != null) todo.changContent(requestDto.getContent());
        todo.changeUpdateAt();

        return TodoResponseDto.of(todo);
    }

    public Todo findTodoById(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않은 일정입니다.")
        );
    }
}
