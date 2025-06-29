package com.gmg.travice.domain.invite.entity;

import com.gmg.travice.domain.plan.entity.Plan;
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
@Table(name = "invite_link")
@Builder
public class InviteLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(unique = true, nullable = false, length = 32)
    private String inviteCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteLinkType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean isActive;

    public enum InviteLinkType {
        VIEW_ONLY,
        EDITABLE
    }
}
