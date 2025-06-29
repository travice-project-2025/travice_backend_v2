package com.gmg.travice.config;

import com.gmg.travice.feature.login.jwt.JWTFilter;
import com.gmg.travice.feature.login.service.CustomOAuth2UserService;
import com.gmg.travice.feature.login.oAuth.handler.CustomSuccessHandler;
import com.gmg.travice.feature.login.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity // security를 활성화 한다
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JWTUtil jwtUtil;
    private final CustomSuccessHandler customSuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, JWTUtil jwtUtil, CustomSuccessHandler customSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.jwtUtil = jwtUtil;
        this.customSuccessHandler = customSuccessHandler;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "JWT-TOKEN", "X-Requested-With", "Cache-Control"));
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "JWT-TOKEN", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    // SecurityFilterChain 인터페이스를 리턴하는 메서드
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf((csrf) -> csrf.disable())
                .formLogin((login) -> login.disable())
                .httpBasic((basic) -> basic.disable());

        http    // csrf 보호 비활성화
                .csrf((csrf) -> csrf.disable());
        http    // from 로그인 방식 안할거다
                .formLogin((login) -> login.disable());
        http    // http basic 인증방식 안할거다
                .httpBasic((basic) -> basic.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        // 루트 경로만 인증 필요 없이 접속 가능
                        .requestMatchers("/").permitAll()
                        // 나머지는 인증 필요
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login((oAuth2) -> oAuth2
                        // Access Token 발급 완료 -> 이걸로 사용자 정보가져와 customOAuth2UserService가 처리
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                    );



        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {

                    System.out.println("=== SecurityConfig AuthenticationEntryPoint 호출 ===");
                    System.out.println("요청 URI: " + request.getRequestURI());
                    System.out.println("HTTP 메서드: " + request.getMethod());
                    System.out.println("Authorization 헤더: " + request.getHeader("Authorization"));

                    // SecurityContext 확인
                    var auth = SecurityContextHolder.getContext().getAuthentication();
                    System.out.println("현재 SecurityContext 인증 상태: " + (auth != null ? auth.getName() + " (인증됨)" : "인증되지 않음"));
                    System.out.println("인증 타입: " + (auth != null ? auth.getClass().getSimpleName() : "없음"));
                    System.out.println("인증 예외: " + authException.getMessage());

                    // 인증 실패 시 401 반환
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                })
        );

        // 객체를 build 타입으로 리턴
        return http.build();
    }
}

