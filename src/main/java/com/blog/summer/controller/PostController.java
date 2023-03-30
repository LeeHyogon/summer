package com.blog.summer.controller;


import com.blog.summer.domain.Post;
import com.blog.summer.dto.PostDto;
import com.blog.summer.dto.RequestPostRegister;
import com.blog.summer.dto.ResponsePostRegister;
import com.blog.summer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ResponsePostRegister> postRegister(@RequestBody RequestPostRegister requestPostRegister) {
        ModelMapper mapper=new ModelMapper();

        PostDto postDto = mapper.map(requestPostRegister, PostDto.class);
        postService.createPost(postDto);

        ResponsePostRegister responsePostRegister=new ResponsePostRegister();
        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostRegister);
    }
}
