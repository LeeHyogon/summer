package com.blog.summer.common;

import com.blog.summer.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;

    public String refreshToken(Long userSeq){
        /*
        Member member = authRepository.findUserByUserSeq(userSeq);

        checkExistence(member);

        return generateToken(member);
         */
        return "";
    }

    private String generateToken(String userId){
        return jwtUtil.generate(userId);
    }
}
