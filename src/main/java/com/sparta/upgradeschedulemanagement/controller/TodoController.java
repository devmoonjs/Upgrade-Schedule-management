package com.sparta.upgradeschedulemanagement.controller;

import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import com.sparta.upgradeschedulemanagement.dto.TodoResponseDto;
import com.sparta.upgradeschedulemanagement.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto requestDto) {
        TodoResponseDto responseDto = todoService.createTodo(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponseDto> getTodo(@PathVariable Long todoId) {
        TodoResponseDto responseDto = todoService.getTodo(todoId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long todoId, @RequestBody TodoRequestDto requestDto) {
        TodoResponseDto responseDto = todoService.updateTodo(todoId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
}
