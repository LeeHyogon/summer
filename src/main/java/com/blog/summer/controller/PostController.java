package com.blog.summer.controller;


import com.blog.summer.domain.Post;
import com.blog.summer.dto.post.PostAllDto;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.RequestPostRegister;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.repository.PostQueryRepository;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final EntityManager em;
    private final PostService postService;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    @PostMapping("/posts")
    public ResponseEntity<ResponsePostRegister> postRegister(@RequestBody RequestPostRegister requestPostRegister) {

        PostDto postDto= PostDto.builder()
                .title(requestPostRegister.getTitle())
                .content(requestPostRegister.getContent())
                .userId(requestPostRegister.getUserId())
                .categoryName(requestPostRegister.getCategoryName())
                .build();
        ResponsePostRegister responsePostRegister = postService.createPost(postDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostRegister);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePostAndMarkAsComment(@PathVariable Long postId){
        postService.deletePostAndMark(postId);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/api/postAll")
    public List<PostAllDto> getPostLazyAll(){
        List<Post> posts = postRepository.findAll();
        List<PostAllDto> result= posts.stream()
                .map(p->new PostAllDto(p))
                .collect(Collectors.toList());
        return result;
    }
}
