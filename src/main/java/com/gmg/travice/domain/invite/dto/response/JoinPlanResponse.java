package com.gmg.travice.domain.invite.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinPlanResponse {
    private Long planId;
    private String title;
    private String message;
}
