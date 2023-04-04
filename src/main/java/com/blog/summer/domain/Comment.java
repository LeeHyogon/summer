package com.blog.summer.domain;


import lombok.Getter;

import javax.persistence.*;
import java.util.Optional;
import java.util.function.Consumer;

@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity user;

    private String body;

    public void setRegisterComment(Post post, UserEntity user, String body) {
        this.post=post;
        this.user=user;
        this.body=body;
    }
}
