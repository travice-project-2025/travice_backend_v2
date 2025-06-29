package com.gmg.travice.domain.plan.dto;

import com.gmg.travice.domain.plan.entity.City;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanListResponseDTO {
    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer memberCount;

    private City city;

    private boolean isPublic;

    private String thumbnail;
}
