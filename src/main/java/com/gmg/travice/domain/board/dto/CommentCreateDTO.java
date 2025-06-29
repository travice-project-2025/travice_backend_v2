package com.gmg.travice.domain.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentCreateDTO {
    private Long id;
    private Long BoardID;
    private String content;
}
