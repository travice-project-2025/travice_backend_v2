package com.gmg.travice.domain.plan.dto;

import com.gmg.travice.domain.plan.entity.City;
import com.gmg.travice.domain.plan.entity.Transport;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanDTO {
    private Long id;

    private Long userId;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer totalDays;

    private Integer memberCount;

//    private Theme theme;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Transport transport;

    private City city;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    private String thumbnail;

    private String region;

    private List<PlanDetailDTO> details;
}
