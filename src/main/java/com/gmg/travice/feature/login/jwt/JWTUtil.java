package com.gmg.travice.feature.login.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    // 1. 비밀키 설정
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String getJWTToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("token", String.class); // token 추출
    }

    public String getName(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class); // name 추출
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            System.out.println("=== JWT 만료 검사 시작 ===");
            System.out.println("현재 시간: " + new Date());

            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            System.out.println("토큰 만료 시간: " + expiration);

            boolean isExpired = expiration.before(new Date());
            System.out.println("토큰 만료 여부: " + isExpired);

            if (!isExpired) {
                long remainingTime = expiration.getTime() - new Date().getTime();
                System.out.println("남은 시간(초): " + (remainingTime / 1000));
            }

            System.out.println("=== JWT 만료 검사 완료 ===");
            return isExpired;

        } catch (Exception e) {
            System.out.println("❌ JWT 파싱 오류: " + e.getMessage());
            e.printStackTrace();
            return true; // 파싱 실패 시 만료된 것으로 처리
        }
    }

    public String createJWT(String token, String name, String email, Long expiredMs) {
        return Jwts.builder()
                .claim("token", token)
                .claim("name", name)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs * 1000))
                .signWith(secretKey)
                .compact();
    }

}
