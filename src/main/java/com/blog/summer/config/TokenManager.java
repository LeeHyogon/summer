package com.blog.summer.config;

import com.blog.summer.domain.Token;
import com.blog.summer.dto.TokenDto;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.exception.TokenCheckFailException;
import com.blog.summer.exception.message.ExceptionMessage;
import com.blog.summer.repository.TokenRepository;
import com.blog.summer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final Environment env;



    // Refresh Token을 발급하는 메서드
    public String generateRefreshToken(String userId) {

        // Refresh Token 생성 로직을 구현합니다.
        Token token = tokenRepository.save(
                Token.builder()
                        .id(userId)
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(Integer.valueOf(env.getProperty("refresh_token.expiration_time")))
                        .build()
        );
        return token.getRefresh_token();
    }

    public Token validRefreshToken(String userId, String refreshToken)  {
        Token token = tokenRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {

            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    public TokenDto refreshAccessToken(TokenDto token,String userId) {

        Token refreshToken = validRefreshToken(userId, token.getRefresh_token());
        if (refreshToken != null) {
            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(userId))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            throw new NotFoundException("로그인을 해주세요");
        }
    }

    public Authentication getAuthentication(String jwt) {
        String userId = jwtProvider.parseToken(jwt);

        if (userId == null) {
            throw new TokenCheckFailException(ExceptionMessage.TOKEN_VALID_TIME_EXPIRED);
        }

        UserDetails userDetails = userService.loadUserByUsername(userId);

        if (!userDetails.getUsername().equals(userId)) {
            throw new TokenCheckFailException(ExceptionMessage.MISMATCH_USERNAME_TOKEN);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
