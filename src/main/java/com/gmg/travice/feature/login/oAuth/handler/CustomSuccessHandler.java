package com.gmg.travice.feature.login.oAuth.handler;

import com.gmg.travice.feature.login.oAuth.user.CustomOAuth2User;
import com.gmg.travice.feature.login.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증이 성공 핸들러
@Component
// SimpleUrlAuthenticationSuccessHandler : 인증 성공 핸들러 클래스
// 인증 성공 후 사용자를 특정 URL로 Redirect 하는 기능 제공
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //소셜 로그인 인증 성공 시 호출되는 메서드
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("CustomSuccessHandler onAuthenticationSuccess");
        // getPrincipal : 인증된 사용자 객체를 가져온다.
        // 이를 CustomAuth2User 타입으로 형변환
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String token = customOAuth2User.getToken();
        String name = customOAuth2User.getName();
        String email = customOAuth2User.getEmail();

        System.out.println("token: " + token + " : " +  "name: " + name+ " : " +  "email: " + email);

        String JWTToken = jwtUtil.createJWT(token, name, email, 60*60*60L);

        response.addCookie(createCookie("JWT-TOKEN", JWTToken));

        response.sendRedirect("http://localhost:5173/plans");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        return cookie;
    }
}
