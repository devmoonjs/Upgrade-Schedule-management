package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.*;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.entity.UserRoleEnum;
import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import com.sparta.upgradeschedulemanagement.exception.EntityNotFoundException;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final WeatherService weatherService;

    // 일정 생성
    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId());
        if (user != null) {
            LocalDate localDate = LocalDate.now();
            String dateTime = getDateformat(localDate);

            String weather = weatherService.getWeatherData(dateTime);

            Todo todo = new Todo(requestDto, weather);
            return TodoResponseDto.of(todoRepository.save(todo));
        }
        throw new RuntimeException();
    }

    // 생성일 "MM-DD" 형태 변환
    private static String getDateformat(LocalDate localDate) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM-dd");
        String dateTime = localDate.format(dateTimeFormat);
        return dateTime;
    }

    // 일정 단건 조회
    public TodoInfoResponseDto getTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new EntityNotFoundException("일정을 찾을 수 없습니다.")
        );

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
        validAdmin(httpServletRequest);

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
                () -> new EntityNotFoundException("존재하지 않은 일정입니다.")
        );
    }

    // 일정 삭제
    public void deleteTodo(Long todoId, HttpServletRequest httpServletRequest) {
        // 관리자 체크
        validAdmin(httpServletRequest);

        Todo todo = findTodoById(todoId);
        todoRepository.delete(todo);
    }

    // 유저 등록
    public void registerManager(RegisterManagerRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 사용자입니다.")
        );

        Todo todo = todoRepository.findById(requestDto.getTodoId()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 일정입니다.")
        );

        UserTodo userTodo = new UserTodo(todo, user);
        usertodoRepository.save(userTodo);
    }

    // 관리자 확인
    public void validAdmin(HttpServletRequest httpServletRequest) {
        String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);
        String token = jwtUtil.subStringToken(tokenValue);
        Claims info = jwtUtil.getUserInfoFromToken(token);

        User user = userRepository.findByName(info.getSubject()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 사용자입니다.")
        );

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalAccessError("수정 및 삭제 권한이 없습니다.");
        }
    }
}
