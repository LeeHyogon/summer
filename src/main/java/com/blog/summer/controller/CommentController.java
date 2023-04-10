package com.blog.summer.controller;


import com.blog.summer.domain.Comment;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.RequestCommentRegister;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.repository.UserRepository;
import com.blog.summer.service.CommentService;
import com.blog.summer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping("/comment")
    public ResponseEntity<ResponseCommentRegister> commentRegister(@RequestBody RequestCommentRegister commentRegister){

        CommentDto commentDto= CommentDto.builder()
                .postId(commentRegister.getPostId())
                .body(commentRegister.getBody())
                .build();
        ResponseCommentRegister responseComment = commentService.createComment(commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
    }
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deletePost(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }




}
