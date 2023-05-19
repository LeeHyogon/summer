package com.blog.summer.common;

import com.blog.summer.common.exception.InvalidRefreshTokenException;
import com.blog.summer.common.exception.RefreshTokenRequiredException;
import com.blog.summer.common.util.JwtUtil;
import com.blog.summer.domain.Token;
import com.blog.summer.dto.TokenDto;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final Environment env;
    private final JwtUtil jwtUtil;
    public Token validRefreshToken(String userId, String refreshToken)  {
        Token token = tokenRepository.findById(userId).orElseThrow(RefreshTokenRequiredException::new);

        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            throw new RefreshTokenRequiredException();
        } else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
            if(token.getExpiration() < 10) {
                token.setExpiration(Integer.valueOf(env.getProperty("refresh_token.extend_time")));
                tokenRepository.save(token);
            }
            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                throw new InvalidRefreshTokenException();
            } else {
                return token;
            }
        }
    }

    public TokenDto refreshAccessToken(TokenDto token, String userId) {
        Token refreshToken = validRefreshToken(userId, token.getRefresh_token());
        return TokenDto.builder()
                .access_token(jwtUtil.generateAccessToken(userId))
                .refresh_token(refreshToken.getRefresh_token())
                .build();
    }

    private String generateAccessToken(String userId,String type){
        return jwtUtil.generateAccessToken(userId);
    }
}
