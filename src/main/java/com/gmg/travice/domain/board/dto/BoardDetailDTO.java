package com.gmg.travice.domain.board.dto;

import com.gmg.travice.domain.board.entity.BoardType;
import com.gmg.travice.domain.board.entity.PreferenceGender;
import com.gmg.travice.domain.user.entity.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDTO {
    // User 정보
    private String name;
    private String nickname;
    private Integer age;
    private GenderType gender;
    private String profileImageUrl;
    private Integer travelCount;
    private Integer companiesCount;

    // plan 정보
    private LocalDate startDate;
    private LocalDate endDate;
    private String Location;

    // Board 정보
    private String title;
    private String detail;
    private Integer memberCount;
    private PreferenceGender preferenceGender;
    private Integer preferenceMinAge;
    private Integer preferenceMaxAge;
    private BoardType boardType;
    private Integer viewCount;
    private LocalDate createdAt;
    private Integer cost;
    private boolean isDeleted;

    // Comment 정보
    private List<CommentDTO> comments;
}
