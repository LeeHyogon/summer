package com.blog.summer.service;

import com.blog.summer.domain.Favorite;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.favorite.FavoriteDto;
import com.blog.summer.dto.favorite.ResponseFavoriteClick;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.FavoriteRepository;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public ResponseFavoriteClick createFavorite(FavoriteDto favoriteDto) {
        Long postId = favoriteDto.getPostId();
        String userId = favoriteDto.getUserId();
        Boolean aBoolean = favoriteDto.getABoolean();
        Optional<Post> postOpt = postRepository.findByIdWithUserComment(postId);
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시물을 찾을수없습니다"));
        UserEntity user = userRepository.findByUserId(userId);
        Favorite favorite = new Favorite();
        favorite.setAddFavorite(post,user, aBoolean);
        favoriteRepository.save(favorite);
        return ResponseFavoriteClick.builder()
                .postId(postId)
                .userId(userId)
                .aBoolean(aBoolean)
                .build();
    }
}
