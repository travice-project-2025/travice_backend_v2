package com.gmg.travice.domain.user.entity;

import com.gmg.travice.domain.board.entity.Board;
import com.gmg.travice.domain.board.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String token;

    private String nickname;

    @Column
    private Integer age;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String profileImageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    private Integer travelCount;

    private Integer companionCount;

    private Integer regionCount;

    // User - Board (one to many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    // User - Comment (one to many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

}
