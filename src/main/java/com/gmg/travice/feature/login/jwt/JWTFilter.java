package com.gmg.travice.feature.login.jwt;

import com.gmg.travice.domain.user.dto.UserDTO;
import com.gmg.travice.feature.login.oAuth.user.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// http 요청마자 이 메서드가 딱 한번 실행되는 필터 구현하기
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        System.out.println("=== JWT 필터 시작 ===");
        System.out.println("요청 경로: " + path);
        System.out.println("현재 시간: " + new java.util.Date());

        // 토큰을 담을 변수
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        System.out.println("쿠키 개수: " + (cookies != null ? cookies.length : 0));

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("쿠키 발견: " + cookie.getName() + " = " + cookie.getValue().substring(0, Math.min(20, cookie.getValue().length())) + "...");
                if (cookie.getName().equals("JWT-TOKEN")) {
                    authorization = cookie.getValue();
                    System.out.println("JWT 토큰 찾음!");
                    break;
                }
            }
        }

        if (authorization == null) {
            System.out.println("❌ JWT 토큰이 없음");
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증
        System.out.println("JWT 토큰 만료 확인 중...");
        if (jwtUtil.isExpired(authorization)) {
            System.out.println("❌ JWT 토큰 만료됨");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("✅ JWT 토큰 유효함");

        try {
            String token = jwtUtil.getJWTToken(authorization);
            String name = jwtUtil.getName(authorization);
            String email = jwtUtil.getEmail(authorization);

            System.out.println("사용자 정보 추출 완료: " + name + " (" + email + ")");

            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmail(email);
            userDTO.setToken(token);

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
            Authentication authToken = new UsernamePasswordAuthenticationToken(name, token, customOAuth2User.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("✅ SecurityContext에 인증 정보 설정 완료");

        } catch (Exception e) {
            System.out.println("❌ JWT 처리 중 오류: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== JWT 필터 종료 ===");
        filterChain.doFilter(request, response);
    }
}
