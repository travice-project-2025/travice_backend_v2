package com.gmg.travice.feature.login.controller;

import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.service.UserService;
import com.gmg.travice.feature.login.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        User me = userService.findUserByJWTToken(request);

        if(me != null) {
            String name = me.getName();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("nickname", me.getNickname());
            return ResponseEntity.ok(userInfo);
        }
        return ResponseEntity.ok("JWT token is expired");
    }
}
