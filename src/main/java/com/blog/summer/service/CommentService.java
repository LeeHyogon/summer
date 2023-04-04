package com.blog.summer.service;


import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.repository.CommentRepository;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseCommentRegister createComment(CommentDto commentDto) {
        Long postId = commentDto.getPostId();
        String body = commentDto.getBody();
        Optional<Post> post = postRepository.findByIdWithUser(postId);
        Comment comment = new Comment();
        if(post != null) {
            comment.setRegisterComment(post.get(), post.get().getUser(), body);
        }
        commentRepository.save(comment);
        return ResponseCommentRegister.builder()
                .title(post.get().getTitle())
                .name(post.get().getUser().getName())
                .body(body)
                .build();
    }
}
