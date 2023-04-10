package com.blog.summer.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post favoritePost;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity favoriteUser;

    private Boolean aBoolean;

    public void setAddFavorite(Post post, UserEntity user, Boolean aBoolean) {
        this.favoritePost=post;
        this.favoritePost.addFavorite(this);
        this.favoriteUser=user;
        this.favoriteUser.addFavorite(this);
        this.aBoolean=aBoolean;
    }
}


