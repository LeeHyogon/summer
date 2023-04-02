package com.blog.summer.dto.post;


import com.blog.summer.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePostRegister extends BaseTimeEntity {

    private Long postId;

    private String categoryName;

    private String title;

    private String content;

    private String name;

    public ResponsePostRegister(Long postId, String categoryName, String title, String content, String name) {
        this.postId = postId;
        this.categoryName = categoryName;
        this.title = title;
        this.content = content;
        this.name = name;
    }
}
