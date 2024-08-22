package com.sparta.upgradeschedulemanagement.controller;

import com.sparta.upgradeschedulemanagement.dto.RegisterUserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserResponseDto;
import com.sparta.upgradeschedulemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 유저 생성
    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 담당자 등록
    @PostMapping("/register-users")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        userService.registerUser(requestDto);
        return ResponseEntity.ok().build();
    }
}
