package com.gmg.travice.domain.plan.entity;

import com.gmg.travice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan_member")
@Builder
public class PlanMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    public enum MemberRole {
        OWNER,      // 주최자
        EDITOR,     // 편집 가능
        VIEWER      // 보기 전용
    }

    public enum MemberStatus {
        ACTIVE,     // 활성
        INACTIVE,   // 비활성
        BANNED      // 차단됨
    }
}
