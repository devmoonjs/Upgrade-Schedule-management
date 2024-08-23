package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.config.PasswordEncoder;
import com.sparta.upgradeschedulemanagement.dto.RegisterUserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserResponseDto;
import com.sparta.upgradeschedulemanagement.entity.Todo;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.entity.UserTodo;
import com.sparta.upgradeschedulemanagement.jwt.JwtUtil;
import com.sparta.upgradeschedulemanagement.repository.TodoRepository;
import com.sparta.upgradeschedulemanagement.repository.UserRepository;
import com.sparta.upgradeschedulemanagement.repository.UserTodoRepository;
import jakarta.servlet.http.HttpServletResponse;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    // 유저 생성
    public UserResponseDto createUser(UserRequestDto requestDto, HttpServletResponse res) {
        // 유저 중복 체크
        String name = requestDto.getName();
        if(userRepository.findByName(name).isPresent()){
            throw new IllegalArgumentException("중복된 유저가 존재합니다.");
        };

        // email 중복 체크
        String email = requestDto.getName();
        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("중복된 이메일입니다.");
        };

        // 비밀번호 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassWord(password);

        // JWT 생성
        String token = jwtUtil.createToken(name);
        jwtUtil.addJwtToCookie(token,res);

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
