package com.blog.summer.dto.comment;

import lombok.Data;

@Data
public class CommentDto {

    private Long postId;
    private String body;
}
