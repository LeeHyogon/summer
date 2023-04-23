package com.blog.summer.domain;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PostTag {
    @Id
    @GeneratedValue
    @Column(name = "post_tag_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post tagPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostTag() {
    }

    @Builder
    public PostTag(Post tagPost,Tag tag) {
        this.tagPost=tagPost;
        this.tag = tag;
    }

    public static PostTag createPostTag(Post post ,Tag tag) {
        PostTag postTag = PostTag.builder()
                .tagPost(post)
                .tag(tag)
                .build();
        post.addPostTag(postTag);
        tag.addPostTag(postTag);
        return postTag;
    }

    public void setPost(Post post) {
        tagPost=post;
    }
}
