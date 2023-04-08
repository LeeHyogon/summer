package com.blog.summer.domain;


import com.blog.summer.dto.comment.CommentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    private String username;

    private String body;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public void setRegisterComment(Post post, UserEntity user, String body, String username, CommentStatus registered) {
        this.post=post;
        this.post.addComment(this);
        this.user=user;
        this.user.addComment(this);
        this.body=body;
        this.username=username;
        this.status=registered;
    }
}
