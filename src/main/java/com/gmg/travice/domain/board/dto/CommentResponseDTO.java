package com.gmg.travice.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentResponseDTO {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createdAt;
}
