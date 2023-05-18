package com.blog.summer.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final Environment env;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration_time}")
    private String expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String generate(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
        return token;
    }
    /*
    public String generate(long userSeq, String type) {

        // 1. token 내부에 저장할 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("userSeq", userSeq);
        claims.put("type", type);

        // 2. token 생성일
        final Date createdDate = new Date();

        // 3. token 만료일
        long expirationTime;
        if ("ACCESS".equals(type)) {
            // 테스트용(30분)
            expirationTime = Long.parseLong(expiration) * 1000;
        } else {
            // 테스트용(1시간)
            expirationTime = Long.parseLong(expiration) * 1000 * 2;
        }
        final Date expirationDate = new Date(createdDate.getTime() + expirationTime);


        return Jwts.builder()
                .setClaims(claims)      // 1
                .setIssuedAt(createdDate)       // 2
                .setExpiration(expirationDate)      // 3
                .signWith(key)
                .compact();
    }
    */


    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isExpired(String token) {
        try {
            final Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public String getTypeFromToken(String token) {
        return getClaimsFromToken(token).get("type", String.class);
    }


    public Long getUserSeqFromToken(String token) {
        return getClaimsFromToken(token).get("userSeq", Long.class);
    }

}