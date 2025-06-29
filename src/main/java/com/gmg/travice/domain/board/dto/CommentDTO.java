package com.gmg.travice.domain.board.dto;

import com.gmg.travice.domain.user.entity.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String writer;
    private String wiriterProfileImage;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;

    private GenderType gender;
    private Integer age;
}
