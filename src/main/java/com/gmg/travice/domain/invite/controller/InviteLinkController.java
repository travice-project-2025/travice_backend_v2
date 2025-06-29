package com.gmg.travice.domain.invite.controller;

import com.gmg.travice.domain.invite.dto.response.InviteLinkResponse;
import com.gmg.travice.domain.invite.dto.response.JoinPlanResponse;
import com.gmg.travice.domain.invite.dto.response.PlanInfoResponse;
import com.gmg.travice.domain.invite.entity.InviteLink;
import com.gmg.travice.domain.invite.service.InviteLinkService;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/invite")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InviteLinkController {
    private final InviteLinkService inviteLinkService;
    private final UserService userService;

    // 초대 링크 생성
    @PostMapping("/create")
    public ResponseEntity<?> createInviteLink(
            HttpServletRequest request,
            @RequestParam Long planId,
            @RequestParam InviteLink.InviteLinkType type) {

        try {
            User user = userService.findUserByJWTToken(request);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "인증된 사용자를 찾을 수 없습니다."));
            }

            log.info("초대 링크 생성 요청 - planId: {}, type: {}, userId: {}",
                    planId, type, user.getId());

            InviteLinkResponse response = inviteLinkService.createInviteLink(
                    planId, type, user.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("data", response);

            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("초대 링크 생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "초대 링크 생성에 실패했습니다."));
        }
    }

    // 초대 링크를 통한 여행 계획 참여
    @PostMapping("/join")
    public ResponseEntity<?> joinPlan(
            HttpServletRequest request,
            @RequestParam String code) {

        try {
            User user = userService.findUserByJWTToken(request);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "인증된 사용자를 찾을 수 없습니다."));
            }

            log.info("여행 계획 참여 요청 - inviteCode: {}, userId: {}",
                    code, user.getId());

            JoinPlanResponse response = inviteLinkService.joinPlan(code, user.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("data", response);

            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("여행 계획 참여 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "여행 계획 참여에 실패했습니다."));
        }
    }

    // 초대 링크 정보 조회 (미인증 가능)
    @GetMapping("/info")
    public ResponseEntity<?> getPlanInfo(@RequestParam String code) {

        try {
            log.info("여행 계획 정보 조회 요청 - inviteCode: {}", code);

            PlanInfoResponse response = inviteLinkService.getPlanInfo(code);

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("data", response);

            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("여행 계획 정보 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "여행 계획 정보 조회에 실패했습니다."));
        }
    }

    // 초대 링크 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInviteLink(
            HttpServletRequest request,
            @RequestParam String code) {

        try {
            User user = userService.findUserByJWTToken(request);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "인증된 사용자를 찾을 수 없습니다."));
            }

            log.info("초대 링크 삭제 요청 - inviteCode: {}, userId: {}",
                    code, user.getId());

            // TODO: 권한 확인 로직 추가 (주최자만 삭제 가능)
            inviteLinkService.deleteInviteLink(code);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "초대 링크가 삭제되었습니다."
            ));
        } catch (Exception e) {
            log.error("초대 링크 삭제 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "초대 링크 삭제에 실패했습니다."));
        }
    }

    // 초대 링크 재생성
    @PostMapping("/regenerate")
    public ResponseEntity<?> regenerateInviteLink(
            HttpServletRequest request,
            @RequestParam Long planId,
            @RequestParam InviteLink.InviteLinkType type) {

        try {
            User user = userService.findUserByJWTToken(request);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "인증된 사용자를 찾을 수 없습니다."));
            }

            log.info("초대 링크 재생성 요청 - planId: {}, type: {}, userId: {}",
                    planId, type, user.getId());

            InviteLinkResponse response = inviteLinkService.regenerateInviteLink(
                    planId, type, user.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("data", response);

            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("초대 링크 재생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "초대 링크 재생성에 실패했습니다."));
        }
    }

    // 초대 링크 유효 시간 조회
    @GetMapping("/ttl")
    public ResponseEntity<?> getRemainingTTL(@RequestParam String code) {

        try {
            Long ttl = inviteLinkService.getRemainingTTL(code);

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("ttl", ttl);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("TTL 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "TTL 조회에 실패했습니다."));
        }
    }
}