package com.gmg.travice.domain.plan.repository;

import com.gmg.travice.domain.plan.entity.PlanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {

    // 특정 여행 계획에 특정 유저가 참여하고 있는지 찾음 (중복 참여 방지)
    Optional<PlanMember> findByPlanIdAndUserId(Long planId, Long userId);

    // 특정 여행 계획에 참여한 모든 멤버 목록 조회
    List<PlanMember> findByPlanId(Long planId);

    // 특정 사용자가 참여한 모든 여행 계획의 참여 기록 조회
    List<PlanMember> findByUserId(Long userId);

    // 해당 사용자가 특정 계획에 이미 참여중인지 확인
    boolean existsByPlanIdAndUserIdAndStatus(Long planId, Long userId, PlanMember.MemberStatus status);
}
