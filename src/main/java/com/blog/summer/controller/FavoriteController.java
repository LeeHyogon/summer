package com.blog.summer.controller;


import com.blog.summer.dto.RequestFavorite;
import com.blog.summer.dto.favorite.FavoriteDto;
import com.blog.summer.dto.favorite.ResponseFavoriteClick;
import com.blog.summer.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FavoriteController {


    private final FavoriteService favoriteService;

    @PostMapping("/favorite")
    public ResponseFavoriteClick clickFavorite(@RequestBody RequestFavorite requestFavorite){
        FavoriteDto favoriteDto= FavoriteDto.builder()
                .postId(requestFavorite.getPostId())
                .userId(requestFavorite.getUserId())
                .aBoolean(requestFavorite.getABoolean())
                .build();
        ResponseFavoriteClick favorite = favoriteService.createFavorite(favoriteDto);

        return favorite;

    }
}
