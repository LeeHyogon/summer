package com.blog.summer.controller;


import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.RequestPostRegister;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

}
