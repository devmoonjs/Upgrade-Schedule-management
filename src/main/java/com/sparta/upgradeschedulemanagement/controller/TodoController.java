package com.sparta.upgradeschedulemanagement.controller;

import com.sparta.upgradeschedulemanagement.dto.RegisterManagerRequestDto;
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
        return ResponseEntity.ok().body(todoService.createTodo(requestDto));

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
        return ResponseEntity.ok().body(todoService.getTodoList(pageable));
    }

    // 일정 수정
    @PutMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TodoRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(todoService.updateTodo(todoId, requestDto, httpServletRequest));
    }

    // 일정 삭제
    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId, HttpServletRequest httpServletRequest) {
        todoService.deleteTodo(todoId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    // 담당자 등록
    @PostMapping("/todos/register-user")
    public ResponseEntity<String> registerManager(@RequestBody RegisterManagerRequestDto requestDto) {
        todoService.registerManager(requestDto);
        return ResponseEntity.ok().body("등록 완료");
    }
}
