package com.blog.summer.service;


import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentStatus;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.CommentRepository;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public ResponsePostRegister createPost(PostDto postDto) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();

        //null 처리 예정
        UserEntity user = userRepository.findByUserId(postDto.getUserId()).orElseThrow(() -> new NotFoundException("사용자를 찾을수없습니다"));;

        post.setPostUser(user);
        postRepository.save(post);
        Long postId=post.getId();
        String name=user.getName();

        return getResponsePostRegister(postDto, postId, name);
    }

    public void deletePost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        post.getComments().forEach(comment -> {
            comment.setStatus(CommentStatus.DELETED);
        });
        postRepository.delete(post);
    }

    /*
    public List<ResponseComment> getCommentList(Long postId){
        Optional<Post> postOpt = postRepository.findById(postId);
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByPost(post);
        List<ResponseComment> responseComments = new ArrayList<>();
        comments.forEach(comment -> {
            responseComments.add(
                    ResponseComment.builder()
                            .body(comment.getBody())
                            .name(comment.getUsername())
                            .build()
            );
        });
        return responseComments;
    }
    */

    /*
    public List<ResponseComment> getCommentList(Long postId){
        Optional<Post> postOpt = postRepository.findById(postId);
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        //default_batch_fetch_size 를 설정하고, 삭제 시 Status만 DELETED로 변경해서
        //쿼리 최적화 테스트 중인데 제대로 작동하지 않음...
        List<Comment> comments = post.getComments();
        List<ResponseComment> responseComments = new ArrayList<>();
        comments.forEach(comment -> {
            responseComments.add(
                    ResponseComment.builder()
                            .body(comment.getBody())
                            .name(comment.getUsername())
                            .build()
            );
        });
        return responseComments;
    }
     */



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
