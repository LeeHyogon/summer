package com.blog.summer.domain;


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

    @OneToMany(mappedBy = "postTag")
    private List<PostTag> postTags = new ArrayList<>();


}
