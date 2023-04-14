package com.blog.summer.service;


import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Favorite;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentStatus;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.PostListDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final FavoriteQueryRepository favoriteQueryRepository;
    private final RedisTemplate<String,Long> redisTemplate;

    public ResponsePostRegister createPost(PostDto postDto) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();


        UserEntity user = userRepository.findByUserId(postDto.getUserId()).orElseThrow(() ->
                new NotFoundException("사용자를 찾을 수 없습니다"));
        post.setPostUser(user);
        postRepository.save(post);
        Long postId=post.getId();
        String name=user.getName();

        return getResponsePostRegister(postDto, postId, name);
    }

    public void deletePostAndMark(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        deletePostComments(post);
        commentQueryRepository.deleteCommentsByPostId(postId);
        post.getFavorites().clear();
        favoriteQueryRepository.deleteFavoritesByPostId(postId);
        postRepository.delete(post);
    }
    public Page<PostListDto> getPostAllByCreatedAt(Integer page,Integer size){
        PageRequest pageRequest=PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");
        Page<Post> posts = postRepository.findAllWithUserCountBy(pageRequest);
        Page<PostListDto> toMap = posts.map(post -> PostListDto.builder()
                .title(post.getTitle())
                .categoryName(post.getCategoryName())
                .createdAt(post.getCreatedAt())
                .username(post.getPostUser().getName())
                .build());
        return toMap;
    }

    private static void deletePostFavorite(Post post) {
        Iterator<Favorite> iterator = post.getFavorites().iterator();
        //favorite 부분은 service와 연계해서 어떻게 구현할 지 고민 중 수정 예정
        while(iterator.hasNext()){
            Favorite favorite = iterator.next();
            favorite.getFavoritePost();
            iterator.remove();
        }
    }

    private static void deletePostComments(Post post) {
        Iterator<Comment> iterator = post.getComments().iterator();
        while(iterator.hasNext()){
            Comment comment = iterator.next();
            comment.setStatus(CommentStatus.DELETED);
            iterator.remove();
        }
    }

    private static ResponsePostRegister getResponsePostRegister(PostDto postDto, Long postId, String name) {
        ResponsePostRegister responsePostRegister= ResponsePostRegister.builder()
                .postId(postId)
                .name(name)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();
        return responsePostRegister;
    }

    public void addViewCntToRedis(Long postId) {
        String key = "post:" + postId + ":views";
        //hint 캐시에 값이 없으면 레포지토리에서 조회 있으면 값을 증가시킨다.
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(valueOperations.get(key)==null){
            valueOperations.set(
                    key,
                    postRepository.findById(postId)
                            .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."))
                            .getViews());
        }
        else {
            valueOperations.increment(key);
        }
        log.info("value:{}",valueOperations.get(key));
    }


}
