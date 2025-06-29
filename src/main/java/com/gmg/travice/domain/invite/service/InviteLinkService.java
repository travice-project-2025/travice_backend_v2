package com.gmg.travice.domain.invite.service;

import com.gmg.travice.domain.invite.dto.response.InviteLinkResponse;
import com.gmg.travice.domain.invite.dto.response.JoinPlanResponse;
import com.gmg.travice.domain.invite.dto.response.PlanInfoResponse;
import com.gmg.travice.domain.invite.entity.InviteLink;
import com.gmg.travice.domain.invite.repository.InviteLinkRepository;
import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.plan.entity.PlanMember;
import com.gmg.travice.domain.plan.repository.PlanMemberRepository;
import com.gmg.travice.domain.plan.repository.PlanRepository;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteLinkService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final InviteLinkRepository inviteLinkRepository;
    private final PlanMemberRepository planMemberRepository;

    // 안전한 랜덤키 생성기
    private static final SecureRandom secureRandom = new SecureRandom();
    // Redis 키 prefix
    private static final String INVITE_KEY_PREFIX = "invite:";
    // TTL 7일
    private static final long INVITE_EXPIRATION_DAYS = 7;
    // 랜덤 코드 바이트 수
    private static final int INVITE_CODE_BYTES = 8;

    @Value("${app.base-url:http://localhost:5173}")
    private String baseUrl;

    /**
     * 랜덤 hex 코드 생성
     */
    private String generateRandomHex(int numBytes) {
        byte[] bytes = new byte[numBytes];
        secureRandom.nextBytes(bytes);
        StringBuilder hex = new StringBuilder(2 * numBytes);
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    // 초대 링크 생성
    @Transactional
    public InviteLinkResponse createInviteLink(Long planId, InviteLink.InviteLinkType type, Long userId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 계획입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 주최자인지 확인
        if (!plan.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("여행 계획의 주최자만 초대 링크를 생성할 수 있습니다.");
        }

        // 랜덤 초대 코드 생성
        String inviteCode;
        String redisKey;

        // 중복 방지를 위해 Redis와 DB에 존재하지 않는 코드가 생성될 때까지 반복
        do {
            inviteCode = generateRandomHex(INVITE_CODE_BYTES);
            // redis에 "invite:code"로 저장
            redisKey = INVITE_KEY_PREFIX + inviteCode;
        } while (redisTemplate.hasKey(redisKey) || inviteLinkRepository.existsByInviteCode(inviteCode));

        // Redis에 저장
        String value = String.format("%d:%s", planId, type.name());
        redisTemplate.opsForValue().set(redisKey, value);
        redisTemplate.expire(redisKey, INVITE_EXPIRATION_DAYS, TimeUnit.DAYS);

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(INVITE_EXPIRATION_DAYS);

        // DB에도 저장 (선택적 - 기록 유지를 위해)
        InviteLink inviteLink = InviteLink.builder()
                .plan(plan)
                .inviteCode(inviteCode)
                .type(type)
                .createdBy(user)
                .expiresAt(expiresAt)
                .isActive(true)
                .build();

        inviteLinkRepository.save(inviteLink);

        String shareUrl = String.format("%s/plan/join?code=%s", baseUrl, inviteCode);

        log.info("초대 링크 생성 완료 - planId: {}, inviteCode: {}, type: {}, TTL: {}일",
                planId, inviteCode, type, INVITE_EXPIRATION_DAYS);

        return InviteLinkResponse.builder()
                .inviteCode(inviteCode)
                .expiresAt(expiresAt)
                .type(type)
                .shareUrl(shareUrl)
                .build();
    }

    // 초대 링크로 참여
    @Transactional
    public JoinPlanResponse joinPlan(String inviteCode, Long userId) {
        // 초대 코드 검증
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        String value = redisTemplate.opsForValue().get(redisKey);

        if (value == null) {
            log.warn("유효하지 않은 초대 링크 - inviteCode: {}", inviteCode);
            throw new IllegalArgumentException("유효하지 않거나 만료된 초대 링크입니다.");
        }
        // "invite:code" 값을 가져와 -> planId:type
        String[] parts = value.split(":");
        Long planId = Long.parseLong(parts[0]);
        InviteLink.InviteLinkType type = InviteLink.InviteLinkType.valueOf(parts[1]);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 계획입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 주최자인지 확인
        if (plan.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("여행 계획 주최자는 초대 링크로 참여할 수 없습니다.");
        }

        // 이미 참여 중인지 확인
        boolean alreadyJoined = planMemberRepository.existsByPlanIdAndUserIdAndStatus(
                planId, userId, PlanMember.MemberStatus.ACTIVE);

        if (alreadyJoined) {
            throw new IllegalArgumentException("이미 참여 중인 여행 계획입니다.");
        }

        // 참여자 추가
        PlanMember.MemberRole role = type == InviteLink.InviteLinkType.EDITABLE ?
                PlanMember.MemberRole.EDITOR : PlanMember.MemberRole.VIEWER;

        PlanMember planMember = PlanMember.builder()
                .plan(plan)
                .user(user)
                .role(role)
                .status(PlanMember.MemberStatus.ACTIVE)
                .build();

        planMemberRepository.save(planMember);

        log.info("여행 계획 참여 완료 - planId: {}, userId: {}, inviteCode: {}",
                planId, userId, inviteCode);

        return JoinPlanResponse.builder()
                .planId(planId)
                .title(plan.getTitle())
                .message("여행 계획에 성공적으로 참여했습니다!")
                .build();
    }

    // 초대 코드로 여행 계획 정보 조회
    public PlanInfoResponse getPlanInfo(String inviteCode) {
        // 초대 코드 검증
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        String value = redisTemplate.opsForValue().get(redisKey);

        if (value == null) {
            log.warn("유효하지 않은 초대 링크 - inviteCode: {}", inviteCode);
            throw new IllegalArgumentException("유효하지 않거나 만료된 초대 링크입니다.");
        }

        String[] parts = value.split(":");
        Long planId = Long.parseLong(parts[0]);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 계획입니다."));

        log.info("여행 계획 정보 조회 - planId: {}, inviteCode: {}", planId, inviteCode);

        return PlanInfoResponse.builder()
                .ownerNickname(plan.getUser().getNickname())
                .title(plan.getTitle())
                .startDate(plan.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .endDate(plan.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .memberCount(plan.getMemberCount())
                .build();
    }

    // 초대 링크 삭제
    public void deleteInviteLink(String inviteCode) {
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        redisTemplate.delete(redisKey);

        // DB에서도 비활성화
        inviteLinkRepository.findByInviteCode(inviteCode)
                .ifPresent(link -> {
                    link.setActive(false);
                    inviteLinkRepository.save(link);
                });

        log.info("초대 링크 삭제 완료 - inviteCode: {}", inviteCode);
    }

    // 초대 링크 남은 TTL 조회
    public Long getRemainingTTL(String inviteCode) {
        String redisKey = INVITE_KEY_PREFIX + inviteCode;
        return redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
    }

    // 초대 링크 재생성
    @Transactional
    public InviteLinkResponse regenerateInviteLink(Long planId, InviteLink.InviteLinkType type, Long userId) {
        // 기존 링크 비활성화
        inviteLinkRepository.findByPlanIdAndType(planId, type)
                .ifPresent(link -> {
                    deleteInviteLink(link.getInviteCode());
                });

        // 새 링크 생성
        return createInviteLink(planId, type, userId);
    }
}
