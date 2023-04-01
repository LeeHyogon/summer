package com.blog.summer.service;


import com.blog.summer.domain.Member;
import com.blog.summer.domain.Post;
import com.blog.summer.dto.PostDto;
import com.blog.summer.dto.ResponsePostRegister;
import com.blog.summer.repository.MemberRepository;
import com.blog.summer.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public ResponsePostRegister createPost(PostDto postDto) {
        String username = postDto.getAuthor();
        Member member = memberRepository.findByUsername(username);

        return null;
    }
}
