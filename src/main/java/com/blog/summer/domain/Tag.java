package com.blog.summer.domain;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private List<PostTag> postTags = new ArrayList<>();

    public Tag() {
    }

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    public void addPostTag(PostTag postTag) {
        postTags.add(postTag);
    }
}
