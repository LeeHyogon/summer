package com.blog.summer.dto.post;

import com.blog.summer.domain.BaseTimeEntity;
import lombok.Data;

@Data
public class PostDto extends BaseTimeEntity {
    private String title;
    private String content;
    private String userId;
    private String categoryName;
}
