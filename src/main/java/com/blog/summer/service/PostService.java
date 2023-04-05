package com.blog.summer.service;


import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public ResponsePostRegister createPost(PostDto postDto) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();

        //null 처리 예정
        UserEntity user = userRepository.findByUserId(postDto.getUserId());

        post.setUser(user);
        postRepository.save(post);
        Long postId=post.getId();
        String name=user.getName();

        return getResponsePostRegister(postDto, postId, name);
    }

    public void deletePost(Long postId){
        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
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
