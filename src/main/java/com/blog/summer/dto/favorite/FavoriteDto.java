package com.blog.summer.dto.favorite;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDto {
    Long postId;
    String userId;
    Boolean aBoolean;
}
