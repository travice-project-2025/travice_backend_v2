package com.gmg.travice.domain.invite.dto.request;

import com.gmg.travice.domain.invite.entity.InviteLink;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInviteLinkRequest {
    private Long planId;
    private InviteLink.InviteLinkType type;
}
