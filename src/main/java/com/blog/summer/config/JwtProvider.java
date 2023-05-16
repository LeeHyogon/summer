package com.blog.summer.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.blog.summer.config.TokenUtil.AVAILABLE;
import static com.blog.summer.config.TokenUtil.EXPIRED;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final Environment env;

    // 토큰 생성
    public String createToken(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
        return token;
    }

    // 토큰에 담겨있는 유저 account 획득
    public List<String> getAccount(String token) {
        // 만료된 토큰에 대해 parseClaimsJws를 수행하면 io.jsonwebtoken.ExpiredJwtException이 발생한다.
        List<String> ret=new ArrayList<>();
        try {
            Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            log.info("만료된 AccessToken입니다. {}",e.getClaims().getSubject(),e);
            ret.add(e.getClaims().getSubject());
            ret.add(EXPIRED);
            return ret;
        } catch (Exception e) {
            log.info("Access token 에러 : .",e);
        }
        String userId = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(token).getBody().getSubject();
        ret.add(userId);
        ret.add(AVAILABLE);
        return ret;
    }

    public String parseToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            log.info("만료된 AccessToken입니다. {}",e.getClaims().getSubject(),e);
            return null;
        } catch (Exception e) {
            log.info("Access token 에러 : .",e);
        }
        return Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody().getSubject();
    }
}
