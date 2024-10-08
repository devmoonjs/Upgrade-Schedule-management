package com.sparta.upgradeschedulemanagement.jwt;

import com.sparta.upgradeschedulemanagement.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Header Key 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 Key
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // Token 만료 시간
    public static final Long TOKEN_TIME = 60 * 60 * 1000L;
    private final HttpServletResponse httpServletResponse;

    @Value("${jwt.secret.key}")
    private String secreteKey;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    public JwtUtil(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secreteKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String name, UserRoleEnum roleEnum) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(name) // 사용자 식별값
                        .claim(AUTHORIZATION_KEY, roleEnum) // 사용자 권한 키 밸류
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm) // 암호화
                        .compact();
    }

    // JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse response) {
        try {
            token = URLEncoder.encode(token, "UTF-8").replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
            cookie.setPath("/");

            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("유효하지 않은 JWT");
        } catch (ExpiredJwtException e) {
            logger.error("만료된 토큰");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 토큰");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            logger.error("잘못된 토큰");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return false;
    }

    // HttpServletRequest 에서 Cookie Value -> JWT 추출
    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public String subStringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        return null; // 토큰이 없을 경우
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
