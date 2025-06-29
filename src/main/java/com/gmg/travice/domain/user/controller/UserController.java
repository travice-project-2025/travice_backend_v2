package com.gmg.travice.domain.user.controller;

import com.gmg.travice.domain.user.dto.UserUpdateDTO;
import com.gmg.travice.domain.user.entity.GenderType;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        User me = userService.findUserByJWTToken(request);

        Map<String, Object> data = new HashMap<>();
        data.put("name", me.getName());
        data.put("email", me.getEmail());
        data.put("nickname", me.getNickname());
        data.put("gender", me.getGender());
        data.put("age", me.getAge());
        data.put("profileImageUrl", me.getProfileImageUrl());
        data.put("tripCount", me.getTravelCount());
        data.put("companionCount", me.getCompanionCount());
        data.put("regionCount", me.getRegionCount());
        System.out.println(data);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> UpdateUserInfo(HttpServletRequest request, @RequestBody UserUpdateDTO updateDTO) {
        try {
            User me = userService.findUserByJWTToken(request);

            if (me == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "인증된 사용자를 찾을 수 없습니다."));
            }

            if (updateDTO.getNickname() != null) {
                me.setNickname(updateDTO.getNickname());
            }

            if (updateDTO.getGender() != null) {
                if (updateDTO.getGender().equals("M")) {
                    me.setGender(GenderType.M);
                } else {
                    me.setGender(GenderType.W);
                }
            }

            if (updateDTO.getAge() != null) {
                me.setAge(updateDTO.getAge());
            }

            userService.updateUser(me);

            // 간단한 성공 응답만 반환
            return ResponseEntity.ok(Map.of("success", true, "message", "프로필이 성공적으로 업데이트되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

}
