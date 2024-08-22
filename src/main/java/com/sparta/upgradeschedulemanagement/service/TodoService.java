package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import com.sparta.upgradeschedulemanagement.dto.TodoResponseDto;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;

    // 일정 생성
    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId());
        if (user != null) {
            Todo todo = new Todo(requestDto);
            return TodoResponseDto.of(todoRepository.save(todo));
        }
        throw new RuntimeException();
    }

    // 일정 단건 조회
    public TodoResponseDto getTodo(Long todoId) {
        return TodoResponseDto.of(findTodoById(todoId));
    }

    // 일정 전체 조회
    public List<TodoResponseDto> getTodoList(Pageable pageable) {
        Page<Todo> todoValue = todoRepository.findAll(pageable);
        List<TodoResponseDto> todoList = new ArrayList<>();

        for (Todo todo : todoValue) {
            todoList.add(TodoResponseDto.of(todo));
        }
        return todoList;
    }

    // 일정 수정
    public TodoResponseDto updateTodo(Long todoId, TodoRequestDto requestDto) {
        Todo todo = findTodoById(todoId);
        if (requestDto.getUserId() != null) todo.changeUserId(requestDto.getUserId());
        if (requestDto.getTitle() != null) todo.changeTitle(requestDto.getTitle());
        if (requestDto.getContent() != null) todo.changContent(requestDto.getContent());
        todo.changeUpdateAt();

        return TodoResponseDto.of(todo);
    }

    // 일정 존재 유뮤 체크
    public Todo findTodoById(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않은 일정입니다.")
        );
    }

    // 일정 삭제
    public void deleteTodo(Long todoId) {
        Todo todo = findTodoById(todoId);
        todoRepository.delete(todo);
    }
}
