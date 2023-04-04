package com.blog.summer.controller;


import com.blog.summer.domain.Comment;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.RequestCommentRegister;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.service.CommentService;
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
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<ResponseCommentRegister> commentRegister(@RequestBody RequestCommentRegister commentRegister){
        ModelMapper mapper = new ModelMapper();
        CommentDto commentDto = mapper.map(commentRegister, CommentDto.class);
        ResponseCommentRegister responseComment = commentService.createComment(commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
    }
}