package com.gmg.travice.domain.invite.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanInfoResponse {
    private String ownerNickname;
    private String title;
    private String startDate;
    private String endDate;
    private Integer memberCount;
}
