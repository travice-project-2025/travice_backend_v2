package com.gmg.travice.domain.board.dto;

import com.gmg.travice.domain.board.entity.PreferenceGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateDTO {
    private String title;
    private String detail;
    private Integer memberCount;
    private PreferenceGender preferenceGender;
    private Integer preferenceMinAge;
    private Integer preferenceMaxAge;
    private Integer cost;
}
