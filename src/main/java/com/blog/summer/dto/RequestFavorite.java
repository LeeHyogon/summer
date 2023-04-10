package com.blog.summer.dto;


import lombok.Data;

@Data
public class RequestFavorite {
    Long postId;
    String userId;
    Boolean aBoolean;
}
