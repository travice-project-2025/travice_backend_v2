package com.gmg.travice.domain.board.dto;

import com.gmg.travice.domain.board.entity.PreferenceGender;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateDTO {
    private String title;
    private String detail;
    private Integer memberCount;
    private PreferenceGender preferenceGender;
    private Integer preferenceMinAge;
    private Integer preferenceMaxAge;
    private Integer cost;
    private Long planId;

}
