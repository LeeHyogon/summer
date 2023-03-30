package com.blog.summer.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePostRegister {

    private Long postId;

    private String title;

    private String content;

    private String author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
