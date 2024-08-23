package com.sparta.upgradeschedulemanagement.filter;

import com.sparta.upgradeschedulemanagement.entity.User;
import com.sparta.upgradeschedulemanagement.jwt.JwtUtil;
import com.sparta.upgradeschedulemanagement.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class AuthFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String url = httpServletRequest.getRequestURI();

        log.info("AuthFilter 실행 ---------------------------");
        if (StringUtils.hasText(url) && (url.startsWith("/api/users"))) {
            log.info("인증 처리 하지 않는 URL : " + url);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) {
                String token = jwtUtil.subStringToken(tokenValue);

                // 토큰 검증
                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("토큰 에러");
                }

                Claims info = jwtUtil.getUserInfoFromToken(token);

                User user = userRepository.findByName(info.getSubject()).orElseThrow(
                        () -> new NullPointerException("존재하지 않은 유저 입니다."));

                servletRequest.setAttribute("user", user);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                throw new IllegalArgumentException("토큰을 찾을 수 없습니다.");
            }
        }
    }
}
