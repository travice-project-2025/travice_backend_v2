package com.gmg.travice.domain.board.entity;

import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Board - Comment ( one to many )
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Lob
    private String title;

    private String detail;

    private Integer memberCount;

    @Enumerated(EnumType.STRING)
    private PreferenceGender preferenceGender;

    private Integer preferenceMinAge;

    private Integer preferenceMaxAge;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private Integer viewCount;

    private LocalDate createdAt;

    private boolean isDeleted;

    private Integer cost;
}
