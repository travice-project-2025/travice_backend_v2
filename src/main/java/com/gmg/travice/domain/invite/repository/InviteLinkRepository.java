package com.gmg.travice.domain.invite.repository;

import com.gmg.travice.domain.invite.entity.InviteLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteLinkRepository extends JpaRepository<InviteLink, Long> {

    // invite_code 가 일치하는 초대링크를 조회
    Optional<InviteLink> findByInviteCode(String inviteCode);

    // 이미 생성된 초대 링크가 있는지 확인
    // 중복 생성 방지용임.
    Optional<InviteLink> findByPlanIdAndType(Long planId, InviteLink.InviteLinkType type);

    // 해당 초대 코드가 DB 에 존재하는지 체크
    // redis는 TTL이 있으므로 현재 유효한 초대린크 중복 방지용
    boolean existsByInviteCode(String inviteCode);
}
