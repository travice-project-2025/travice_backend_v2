package com.gmg.travice.domain.board.dto;

import com.gmg.travice.domain.board.entity.BoardType;
import com.gmg.travice.domain.board.entity.PreferenceGender;
import com.gmg.travice.domain.user.entity.GenderType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListDTO {
    private Long id;

    // Board에서 가져올 정보
    private String title;

    private String detail;

    private Integer memberCount;

    private PreferenceGender preferredGender;

    private Integer preferenceMaxAge;

    private Integer preferenceMinAge;

    private Integer viewCount;

    private BoardType boardType;

    private boolean deleted;

    private LocalDate createdAt;

    // Plan 에서 가져올 정보
    private String Location;

    private LocalDate startDate;

    private LocalDate endDate;


    // User 에서 가져올 정보
    private String writer;

    private GenderType gender;

    private Integer age;



}
