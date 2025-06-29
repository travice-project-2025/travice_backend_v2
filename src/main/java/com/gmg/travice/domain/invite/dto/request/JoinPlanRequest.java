package com.gmg.travice.domain.invite.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinPlanRequest {
    private String inviteCode;
}
