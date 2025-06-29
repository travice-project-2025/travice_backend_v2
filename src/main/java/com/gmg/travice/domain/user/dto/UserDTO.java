package com.gmg.travice.domain.user.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;

    private String email;

    private String name;

    private String token;

    private String nickname;

    private Integer age;

    private String gender;

    private String profileImageUrl;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;

}
