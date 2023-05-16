package com.blog.summer.config;

import com.blog.summer.dto.TokenDto;
import com.blog.summer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.blog.summer.config.TokenUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenManager tokenManager;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
         /*
        Cookie[] cookies = request.getCookies();
        String accessToken=null;
        String refreshToken=null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(ACCESS_TOKEN_HEADER))
                accessToken=cookie.getValue();
            if(cookie.getName().equals(REFRESH_TOKEN_HEADER))
                refreshToken=cookie.getValue();
        }
         */
        String requestURI = request.getRequestURI();
        if ("/login".equals(requestURI)) {
            filterChain.doFilter(request, response); // Skip this filter for /login
        }else {
            String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
            String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
            TokenDto tokenDto = TokenDto.builder()
                    .access_token(accessToken)
                    .refresh_token(refreshToken)
                    .build();

            List<String> ret = jwtProvider.getAccount(accessToken);
            String userId = ret.get(0);
            String status = ret.get(1);
            if (status.equals(EXPIRED)) {
                tokenManager.refreshAccessToken(tokenDto, userId);
            } else if (status.equals(AVAILABLE)) {
            }
            filterChain.doFilter(request, response);
        }
    }

}
