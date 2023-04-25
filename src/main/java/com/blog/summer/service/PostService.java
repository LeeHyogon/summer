package com.blog.summer.service;


import com.blog.summer.domain.*;
import com.blog.summer.dto.comment.CommentStatus;
import com.blog.summer.dto.post.*;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.*;
import com.blog.summer.repository.comment.CommentRepository;
import com.blog.summer.repository.favorite.FavoriteRepository;
import com.blog.summer.repository.post.PostQueryRepository;
import com.blog.summer.repository.post.PostRepository;
import com.blog.summer.repository.postTag.PostTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostQueryRepository postQueryRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final RedisTemplate redisTemplate;

    public ResponsePostRegister createPost(PostDto postDto) {
        List<String> tagNames = postDto.getTagNames();
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();

        UserEntity user = userRepository.findByUserId(postDto.getUserId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));
        post.setPostUser(user);
        postRepository.save(post);
        for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresentOrElse(
                    (tag)->{
                        createPostTag(post, tag);
                    },
                    ()->{
                        Tag tag = Tag.builder()
                                .name(tagName)
                                .build();
                        tagRepository.save(tag);
                        createPostTag(post, tag);
                    }
            );
        }
        Long postId=post.getId();
        String name=user.getName();

        return getResponsePostRegister(postDto, postId, name);
    }

    private PostTag createPostTag(Post post, Tag tag) {
        PostTag postTag = PostTag.createPostTag(post, tag);
        postTagRepository.save(postTag);
        return postTag;
    }

    //태그 지우는 것 구현 남음.
    //post 엔티티 변경 감지 로직 남음.
    //만약 기존에 존재하던 태그를 지운다면?
    //벨로그는 기존에 존재하던 태그 청소는 나중에 하는 것으로 확인
    //글 태그 삭제 시 바로 반영 안됨.
    //글 태그 추가 시 바로 반영 안됨.
    //글 생성 시 태그도 바로 추가 안됨...이미 존재하던 태그가 아니면 생성 처리를 좀 늦게하는 로직 인건지 확인중
    public void updatePost(PostUpdateDto postUpdateDto){
        Long postId = postUpdateDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        String title = postUpdateDto.getTitle();
        String content = postUpdateDto.getContent();
        List<String> tagNames = postUpdateDto.getTagNames();
        for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresentOrElse(
                    (tag)->{
                        //태그가 존재할 경우에, post 수정 시 이미 존재 하던 태그를
                        //post에 입력한 경우
                        //postTag 생성해서 연관관계 설정.
                        postTagRepository.findByPostAndTag(postId,tag.getId()).orElseGet(
                                ()-> {
                                    return createPostTag(post, tag);
                                }
                        );
                        //PostTag postTag = PostTag.createPostTag(post,tag);
                        //postTagRepository.save(postTag);
                    },
                    //태그가 존재하지 않으면 create와 동일한 로직으로
                    //태그를 생성 후 , post에 저장
                    ()->{
                        Tag tag = Tag.builder()
                                .name(tagName)
                                .build();
                        tagRepository.save(tag);
                        createPostTag(post, tag);
                    }
            );
        }
    }

    public ResponsePostOne getPostOne(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        ResponsePostOne responsePostOne= ResponsePostOne.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .name(post.getPostUser().getName())
                .categoryName(post.getCategoryName())
                .build();
        return responsePostOne;
    }

    public void deletePostAndMark(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        deletePostComments(post);
        commentRepository.deleteCommentsByPostId(postId);
        post.getFavorites().clear();
        favoriteRepository.deleteFavoritesByPostId(postId);
        postRepository.delete(post);
    }

    public Page<PostListDto> getPostAllByCreatedAt(Integer page,Integer size){
        PageRequest pageRequest=PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");
        Page<Post> posts = postRepository.findPostsWithUsersAsPage(pageRequest);
        Page<PostListDto> toMap = posts.map(post -> PostListDto.builder()
                .title(post.getTitle())
                .categoryName(post.getCategoryName())
                .createdAt(post.getCreatedAt())
                .username(post.getPostUser().getName())
                .build());
        return toMap;
    }

    private static void deletePostFavorite(Post post) {
        Iterator<Favorite> iterator = post.getFavorites().iterator();
        //favorite 부분은 service와 연계해서 어떻게 구현할 지 고민 중 수정 예정
        while(iterator.hasNext()){
            Favorite favorite = iterator.next();
            favorite.getFavoritePost();
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

    public void addViewCntToRedis(Long postId) {
        String key = getKey(postId);
        //hint 캐시에 값이 없으면 레포지토리에서 조회 있으면 값을 증가시킨다.
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(valueOperations.get(key)==null){
            valueOperations.set( key,
                    String.valueOf(postRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.")).getViews()),
                    Duration.ofMinutes(10));
            valueOperations.increment(key);
        }
        /*
        else {
            valueOperations.increment(key); //만료시간 지나기 전까진 동일한 key에 대해 중복으로 increment 되지 않는다.
        }
        */
        log.info("value:{}",valueOperations.get(key));
    }


    private static String getKey(Long postId) {
        return "post:" + postId + ":views";
    }

    //3분마다 자동 실행해주는 스케쥴러
    @Scheduled(cron = "0 0/1 * * * ?")
    public void deleteViewCntCacheFromRedis() {
        Set<String> redisKeys = redisTemplate.keys("post:*:views");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String numericPart = key.substring(key.indexOf(":") + 1, key.lastIndexOf(":"));
            Long postId = Long.parseLong(numericPart);
            String s = (String) redisTemplate.opsForValue().get(key);
            Long views = Long.parseLong(s);
            //
            postQueryRepository.addViewCntFromRedis(postId,views);
            redisTemplate.delete(key);
            redisTemplate.delete("post:"+postId+"views");
        }
    }
}
