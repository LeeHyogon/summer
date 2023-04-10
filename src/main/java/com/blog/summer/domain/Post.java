package com.blog.summer.domain;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private UserEntity postUser;

    @OneToMany(mappedBy = "commentPost")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "favoritePost")
    private List<Favorite> favorites = new ArrayList<>();

    public Post() {}


    public void setPostUser(UserEntity postUser) {
        this.postUser = postUser;
        this.postUser.addPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Builder
    public Post(String title, String content, String categoryName) {
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }
}

