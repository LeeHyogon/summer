package com.blog.summer.domain;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post extends BaseTimeEntity {


    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Post() {}

    public void setUser(UserEntity user) {
        this.user=user;
        this.user.addPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public IllegalAccessException getComments() {
        return new IllegalAccessException("사용금지 서비스기능으로 사용하세요");
    }

    @Builder
    public Post(String title, String content, String categoryName) {
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }

}

