package com.gmg.travice.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "plan_details")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    private String planDetailName;

    // n일차
    private Integer day;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transportFromPrevious;

    @Lob
    private String memo;

    private Double latitude;

    private Double longitude;

    private String address;

}
