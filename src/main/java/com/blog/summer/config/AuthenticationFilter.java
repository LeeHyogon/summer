package com.blog.summer.config;

import com.blog.summer.dto.user.RequestLogin;
import com.blog.summer.dto.user.UserDto;
import com.blog.summer.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.blog.summer.config.TokenUtil.ACCESS_TOKEN_HEADER;
import static com.blog.summer.config.TokenUtil.REFRESH_TOKEN_HEADER;


@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;
    private JwtProvider jwtProvider;
    private TokenManager tokenManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService
            , Environment env, JwtProvider jwtProvider,TokenManager tokenManager) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
        this.jwtProvider =jwtProvider;
        this.tokenManager=tokenManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {


        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);
        String userId = userDetails.getUserId();
        //이미 존재하면 시간 연장-> 로그인일 때는 매번, 갱신해주는 게 맞는것 같아서 수정.
        String refreshToken = tokenManager.generateRefreshToken(userId);
        String accessToken = jwtProvider.createToken(userId);

        response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(REFRESH_TOKEN_HEADER, refreshToken);
        response.addHeader("userId", userId);
    }
}
