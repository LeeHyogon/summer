package com.blog.summer.domain;


import com.blog.summer.dto.PostTagStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
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

    private String tagName;

    @Enumerated(EnumType.STRING)
    private PostTagStatus status;
    public PostTag() {
    }

    public PostTag(Post tagPost,Tag tag) {
        this.tagPost=tagPost;
        this.tag = tag;
    }

    public PostTag(String tagName, PostTagStatus status) {
        this.tagName = tagName;
        this.status = status;
    }

    public PostTag(Post tagPost, Tag tag, String tagName, PostTagStatus status) {
        this.tagPost = tagPost;
        this.tag = tag;
        this.tagName = tagName;
        this.status = status;
    }

    public static PostTag createPostTag(Post post , Tag tag, String tagName, PostTagStatus status) {
        PostTag postTag = PostTag.builder()
                .tagPost(post)
                .tag(tag)
                .tagName(tagName)
                .status(status)
                .build();
        return postTag;
    }

    public void setPost(Post post) {
        tagPost=post;
    }

    public void setStatus(PostTagStatus postTagStatus) {
        status=postTagStatus;
    }
}
