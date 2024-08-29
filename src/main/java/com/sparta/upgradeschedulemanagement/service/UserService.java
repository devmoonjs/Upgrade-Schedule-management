package com.sparta.upgradeschedulemanagement.service;

import com.sparta.upgradeschedulemanagement.config.PasswordEncoder;
import com.sparta.upgradeschedulemanagement.dto.LoginRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserRequestDto;
import com.sparta.upgradeschedulemanagement.dto.UserResponseDto;
import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.entity.UserRoleEnum;
import com.sparta.upgradeschedulemanagement.exception.DuplicatedEntityException;
import com.sparta.upgradeschedulemanagement.exception.EntityNotFoundException;
import com.sparta.upgradeschedulemanagement.exception.AuthorizedException;
import com.sparta.upgradeschedulemanagement.jwt.JwtUtil;
import com.sparta.upgradeschedulemanagement.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ADMIN TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 유저 생성
    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto, HttpServletResponse res) {
        // 유저 중복 체크
        String name = requestDto.getName();
        if(userRepository.findByName(name).isPresent()){
            throw new DuplicatedEntityException("중복된 유저가 존재합니다.");
        };

        // email 중복 체크
        String email = requestDto.getEmail();
        if(userRepository.findByEmail(email).isPresent()){
            System.out.println("================");
            throw new DuplicatedEntityException("중복된 이메일입니다.");
        };

        UserRoleEnum role = UserRoleEnum.USER;
        String requestKey = requestDto.getAdminKey();

        // ADMIN 키 유무 판별
        if (requestKey != null) {
            if (!requestKey.equals(ADMIN_TOKEN)) {
                throw new IllegalAccessError("ADMIN KEY 오류");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 토큰 생성
        String token = jwtUtil.createToken(name,role);
        jwtUtil.addJwtToCookie(token,res);

        // 비밀번호 인코딩 & 객체 저장
        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto, password, role);

        return UserResponseDto.of(userRepository.save(user));
    }

    // 유저 존재 유무 체크
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않은 담당자 입니다."));
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto requestDto) {
        User user = findById(userId);

        if (requestDto.getName() != null) user.changeName(requestDto.getName());
        if (requestDto.getEmail() != null) user.changeEmail(requestDto.getEmail());

        userRepository.save(user);
        return UserResponseDto.of(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse servletResponse) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 유저 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AuthorizedException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthorizedException("비밀번호가 틀렸습니다.");
        }

        // 로그인 완료 시
        String token = jwtUtil.createToken(user.getName(), user.getRole());
        jwtUtil.addJwtToCookie(token, servletResponse);
    }
}
