package com.gmg.travice.domain.invite.dto.response;

import com.gmg.travice.domain.invite.entity.InviteLink;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteLinkResponse {
    private String inviteCode;
    private LocalDateTime expiresAt;
    private InviteLink.InviteLinkType type;
    private String shareUrl;
}
