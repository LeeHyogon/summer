package com.blog.summer.service;


import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Favorite;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentStatus;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.CommentRepository;
import com.blog.summer.repository.FavoriteRepository;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;

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

    public void deletePostAndMark(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        deletePostComments(post);
        deletePostFavorite(post);
        postRepository.delete(post);
    }

    private static void deletePostFavorite(Post post) {
        Iterator<Favorite> iterator = post.getFavorites().iterator();
        //favorite 부분은 service와 연계해서 어떻게 구현할 지 고민 중 수정 예정
        while(iterator.hasNext()){
            Favorite favorite = iterator.next();
            iterator.remove();
        }
    }

    private static void deletePostComments(Post post) {
        Iterator<Comment> iterator = post.getComments().iterator();
        while(iterator.hasNext()){
            Comment comment = iterator.next();
            comment.setStatus(CommentStatus.DELETED);
            iterator.remove();
        }
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
