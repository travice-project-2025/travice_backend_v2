package com.gmg.travice.domain.plan.dto;

import com.gmg.travice.domain.plan.entity.Transport;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanDetailDTO {

    private Long id;
    private Long planId;
    private String planDetailName;
    private Integer day;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private Transport transportFromPrevious;
    private String memo;

    // 위치 정보
    private Double latitude;
    private Double longitude;
    private String address;
}
