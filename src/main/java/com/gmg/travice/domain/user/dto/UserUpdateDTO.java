package com.gmg.travice.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String nickname;

    private Integer age;

    private String gender;
}
