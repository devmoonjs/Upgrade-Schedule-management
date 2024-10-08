package com.sparta.upgradeschedulemanagement.filter;

import com.sparta.upgradeschedulemanagement.entity.UserRoleEnum;
import com.sparta.upgradeschedulemanagement.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String url = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (url.startsWith("/api/auth")) {
            log.info("========================== 인증처리가 필요 없으므로 필터를 넘어갑니다. ==========================");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        log.info("========================== 필터를 시작합니다. ==========================");
        String bearerJwt = jwtUtil.getTokenFromRequest(httpRequest);

        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.subStringToken(bearerJwt);

        // AMIN 일 경우 '수정' 및 '삭제' 허용토록
        try {
            Claims claims = jwtUtil.extractClaims(jwt);

            List<String> allowedMethods = Arrays.asList("PUT", "DELETE");
            String pathPrefix = "/api/admin/";

            // 경로와 메서드를 체크하고, 관리자 권한이 필요한 경로인지 확인
            if (checkMethodPath(method, url, allowedMethods, pathPrefix)) {
                log.info("========================== [관리자 기능] 이 실행되어 관리자 체크를 진행합니다. ==========================");
                String userRole = claims.get("auth", String.class);
                if (!UserRoleEnum.ADMIN.name().equals(userRole)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 필요합니다."); // 관리자 권한이 없는 경우 403을 반환
                    return;
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 오류가 발생했습니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰 검증 중 오류가 발생했습니다.");
        }
    }

    private boolean checkMethodPath(String method, String url, List<String> allowedMethods, String pathPrefix) {
        if (!checkHttpMethod(method, allowedMethods)) {
            return false;
        }

        if (!checkPathUrl(url, pathPrefix)) {
            return false;
        }

//        String idPart = extractIdFromPath(url, pathPrefix);
        return true;
    }

    // 메서드 유효성 검사
    private boolean checkHttpMethod(String method, List<String> allowedMethods) {
        return allowedMethods.stream().anyMatch(allowedMethod -> allowedMethod.equalsIgnoreCase(method));
    }

    // 경로 유효성 검사
    private boolean checkPathUrl(String url, String pathPrefix) {
        return url.startsWith(pathPrefix);
    }

    // 유저 ID 값만 추출
    private String extractIdFromPath(String url, String pathPrefix) {
        return url.substring(pathPrefix.length());
    }

    // ID 값 유효성 검사 (숫자인지)
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
