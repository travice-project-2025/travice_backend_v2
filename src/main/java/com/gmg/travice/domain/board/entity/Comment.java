package com.gmg.travice.domain.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.gmg.travice.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Lob
    private String content;

    private LocalDateTime createdAt;

    private boolean isDeleted;


}
