package com.blog.summer.dto.comment;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseComment {
    private String body;
    private String name;
}
