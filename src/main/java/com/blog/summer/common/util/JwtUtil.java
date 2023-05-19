package com.blog.summer.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final Environment env;

    public static final String ACCESS_TOKEN_HEADER="accessToken";
    public static final String REFRESH_TOKEN_HEADER="refreshToken";
    public static final String REFRESH_TOKEN_TYPE = "REFRESH";
    public static final String ACCESS_TOKEN_TYPE = "ACCESS";

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration_time}")
    private String expiration;


    /*
    public String generate(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
        return token;
    }
     */

    public String generateAccessToken(String userId) {

        // 1. token 내부에 저장할 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 2. token 생성일
        final Date createdDate = new Date(System.currentTimeMillis());
        // 3. token 만료일
        final Date expirationDate = new Date(System.currentTimeMillis()+
                Long.parseLong(env.getProperty("token.expiration_time")));

        return Jwts.builder()
                .setClaims(claims)      // 1
                .setIssuedAt(createdDate)       // 2
                .setExpiration(expirationDate)      // 3
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
    }



    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
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


    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", String.class);
    }

}