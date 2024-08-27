package com.sparta.upgradeschedulemanagement.controller;

import com.sparta.upgradeschedulemanagement.dto.RegisterUserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.TodoInfoResponseDto;
import com.sparta.upgradeschedulemanagement.dto.TodoRequestDto;
import com.sparta.upgradeschedulemanagement.dto.TodoResponseDto;
import com.sparta.upgradeschedulemanagement.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    // 일정 생성
    @PostMapping("/todos")
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto requestDto) {
        TodoResponseDto responseDto = todoService.createTodo(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 일정 단건 조회
    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoInfoResponseDto> getTodo(@PathVariable Long todoId) {
        return ResponseEntity.ok().body(todoService.getTodo(todoId));
    }

    // 일정 전체 조회
    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponseDto>> getTodoList(
            @PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<TodoResponseDto> todoList = todoService.getTodoList(pageable);
        return ResponseEntity.ok().body(todoList);
    }

    // 일정 수정
    @PutMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TodoRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        TodoResponseDto responseDto = todoService.updateTodo(todoId, requestDto, httpServletRequest);
        if (responseDto == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok().body(responseDto);
    }

    // 일정 삭제
    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId, HttpServletRequest httpServletRequest) {
        if(!todoService.deleteTodo(todoId, httpServletRequest)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok().build();
    }

    // 담당자 등록
    @PostMapping("/todos/register-user")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        todoService.registerUser(requestDto);
        return ResponseEntity.ok().build();
    }
}
