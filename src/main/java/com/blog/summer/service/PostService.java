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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final FavoriteQueryRepository favoriteQueryRepository;

    public ResponsePostRegister createPost(PostDto postDto) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();

        //null 처리 예정
        UserEntity user = userRepository.findByUserId(postDto.getUserId()).orElseThrow(() -> new NotFoundException("사용자를 찾을수없습니다"));;

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




}
