package com.blog.summer.common;

import com.blog.summer.common.util.JwtUtil;
import com.blog.summer.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;



    public TokenDto reGenerateAccessToken(String userId,String refreshToken){
        return jwtUtil.reGenerateAccessToken(userId,refreshToken);
    }
}
