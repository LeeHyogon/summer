package com.blog.summer.service;


import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.exception.NotFoundException;
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
        Optional<Post> postOptional = postRepository.findByIdWithUser(postId);
        Post post = postOptional.orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));
        Comment comment = Comment.builder()
                .post(post)
                .user(post.getUser())
                .body(body)
                .build();

        commentRepository.save(comment);
        return ResponseCommentRegister.builder()
                .commentId(comment.getId())
                .title(post.getTitle())
                .name(post.getUser().getName())
                .body(body)
                .build();
    }

    public void deleteComment(Long commentId){
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }
}
