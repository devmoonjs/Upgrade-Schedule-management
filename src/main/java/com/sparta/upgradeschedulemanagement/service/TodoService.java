package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.*;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.entity.UserRoleEnum;
import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import com.sparta.upgradeschedulemanagement.jwt.JwtUtil;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import com.sparta.upgradeschedulemanagement.repository.UserRepository;
import com.sparta.upgradeschedulemanagement.repository.UserTodoRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserTodoRepository usertodoRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

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
    public TodoInfoResponseDto getTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow();
        List<UserTodo> userTodoList = usertodoRepository.findUserTodosByTodo(todo);

        List<UserInfoDto> responseDtos = userTodoList.stream().map(it ->
                UserInfoDto.of(it.getUser())).toList();

        return TodoInfoResponseDto.builder()
                .title(userTodoList.get(0).getTodo().getTitle())
                .content(userTodoList.get(0).getTodo().getContent())
                .users(responseDtos)
                .build();
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
    public TodoResponseDto updateTodo(Long todoId, TodoRequestDto requestDto, HttpServletRequest httpServletRequest) {
        // 관리자 체크
        if(!validAdmin(httpServletRequest)) {
            return null;
        }

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
    public boolean deleteTodo(Long todoId, HttpServletRequest httpServletRequest) {
        // 관리자 체크
        if(!validAdmin(httpServletRequest)) {
            return false;
        }
        Todo todo = findTodoById(todoId);
        todoRepository.delete(todo);
        return true;
    }


    // 유저 등록
    public void registerUser(RegisterUserRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(); // 유저 체크
        Todo todo = todoRepository.findById(requestDto.getTodoId()).orElseThrow(); // 일정 체크

        UserTodo userTodo = new UserTodo();
        userTodo.setUser(user);
        userTodo.setTodo(todo);
        usertodoRepository.save(userTodo);
    }

    // 토큰 유저가 관리자인지 확인
    public boolean validAdmin(HttpServletRequest httpServletRequest) {
        String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);
        String token = jwtUtil.subStringToken(tokenValue);
        Claims info = jwtUtil.getUserInfoFromToken(token);
        User user = userRepository.findByName(info.getSubject()).orElseThrow();
        return user.getRole().equals(UserRoleEnum.ADMIN);
    }
}
