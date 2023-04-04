package com.blog.summer.service;


import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.post.PostDto;
import com.blog.summer.dto.post.ResponsePostRegister;
import com.blog.summer.repository.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public ResponsePostRegister createPost(PostDto postDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Post post = mapper.map(postDto, Post.class);
        UserEntity user = userRepository.findByUserId(postDto.getUserId());
        post.setUser(user);
        postRepository.save(post);
        Long postId=post.getId();
        String name=user.getName();

        return getResponsePostRegister(postDto, postId, name);
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
}
