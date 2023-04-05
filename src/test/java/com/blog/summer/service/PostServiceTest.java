package com.blog.summer.service;

import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;
    @Test
    @Transactional
    void createPost() {

        ResponsePostRegister responsePost = createAndGetResponsePostRegister();
        Optional<Post> postOpt = postRepository.findById(responsePost.getPostId());
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        assertEquals(responsePost.getPostId(), post.getId());
        assertEquals(responsePost.getTitle(), post.getTitle());
        assertEquals(responsePost.getContent(), post.getContent());
        assertEquals(responsePost.getCategoryName(),post.getCategoryName());
    }

    private ResponsePostRegister createAndGetResponsePostRegister() {
        PostDto postDto=PostDto.builder()
                .title("테스트제목")
                .content("테스트내용")
                .userId("c7e2f268-29a3-4fc8-9a9a-517392e018a6")
                .categoryName("테스트카테고리")
                .build();
        ResponsePostRegister responsePost = postService.createPost(postDto);
        return responsePost;
    }
    @Test
    @Transactional
    void deletePost() {
        ResponsePostRegister responsePost = createAndGetResponsePostRegister();
        Optional<Post> postOpt = postRepository.findById(responsePost.getPostId());
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Long id = post.getId();
        UserEntity user = post.getUser();
        postRepository.delete(post);
        assertEquals(Optional.empty(),postRepository.findById(id));
        assertEquals(user,userRepository.findByUserId(user.getUserId()));
    }
}