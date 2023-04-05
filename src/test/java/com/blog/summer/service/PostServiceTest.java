package com.blog.summer.service;

import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.CommentRepository;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Test
    @Transactional
    void createPostandComment() {
        ResponsePostRegister responsePost = createAndGetResponsePostRegister();
        Optional<Post> postOpt = postRepository.findById(responsePost.getPostId());
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Long postId = post.getId();
        ResponseCommentRegister registerComment1 = leaveComment(postId, "댓글1");
        ResponseCommentRegister registerComment2 = leaveComment(postId, "댓글2");
        //em.flush();
        assertEquals(responsePost.getPostId(), postId);
        assertEquals(responsePost.getTitle(), post.getTitle());
        assertEquals(responsePost.getContent(), post.getContent());
        assertEquals(responsePost.getCategoryName(),post.getCategoryName());
        List<Comment> comments = post.getComments();
        assertEquals(2,post.getComments().size());
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
        Long postId = post.getId();
        ResponseCommentRegister responseComment1 = leaveComment(postId, "댓글1");
        ResponseCommentRegister responseComment2 = leaveComment(postId, "댓글2");

        UserEntity user = post.getUser();
        postRepository.delete(post);
        assertEquals(Optional.empty(),postRepository.findById(postId));
        assertEquals(user,userRepository.findByUserId(user.getUserId()));
    }

    private ResponseCommentRegister leaveComment(Long postId,String body) {
         return commentService.createComment(CommentDto.builder()
                .postId(postId)
                .body(body)
                .build());
    }
}