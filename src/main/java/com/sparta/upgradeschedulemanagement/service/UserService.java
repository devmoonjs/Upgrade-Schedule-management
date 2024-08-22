package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.dto.RegisterUserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserResponseDto;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import com.sparta.upgradeschedulemanagement.repository.UserRepository;
import com.sparta.upgradeschedulemanagement.repository.UserTodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTodoRepository userTodoRepository;
    private final TodoRepository todoRepository;


    // 유저 생성
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = new User(requestDto);
        return UserResponseDto.of(userRepository.save(user));
    }

    // 유저 존재 유무 체크
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않은 담당자 입니다."));
    }

    // 유저 등록
    public void registerUser(RegisterUserRequestDto requestDto) {
        User user = findById(requestDto.getUserId()); // 유저 체크
        Todo todo = todoRepository.findById(requestDto.getTodoId()).orElseThrow();

        UserTodo userTodo = new UserTodo();
        userTodo.setUser(user);
        userTodo.setTodo(todo);
        userTodoRepository.save(userTodo);
    }

    public UserResponseDto updateUser(Long userId, UserRequestDto requestDto) {
        User user = findById(userId);

        if (requestDto.getName() != null) user.changeName(requestDto.getName());
        if (requestDto.getEmail() != null) user.changeEmail(requestDto.getEmail());
        user.changeUpdatedAt();

        userRepository.save(user);
        return UserResponseDto.of(user);
    }

    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }
}
