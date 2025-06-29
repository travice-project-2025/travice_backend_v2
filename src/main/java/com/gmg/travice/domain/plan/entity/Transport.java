package com.gmg.travice.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transport")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String transportName;
}

