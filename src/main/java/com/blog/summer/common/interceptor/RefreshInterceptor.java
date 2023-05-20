package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.AccessTokenRequiredException;
import com.blog.summer.common.exception.RefreshTokenRequiredException;
import com.blog.summer.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.summer.common.util.TokenUtil.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class RefreshInterceptor implements HandlerInterceptor {

    private final TokenUtil tokenUtil;


    @Getter
    @Setter
    protected String uri;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String refreshToken = this.getTokenFromHeader(request, REFRESH_TOKEN_HEADER);
        String token = this.getTokenFromHeader(request, ACCESS_TOKEN_HEADER);
        checkRefreshTokenExist(refreshToken);
        checkAccessTokenExist(token);
        String userId = tokenUtil.getUserIdFromToken(token);
        checkTokenValid(refreshToken, userId);
        setUserIdToAttribute(request,userId);
        setRefreshTokenToAttribute(request,refreshToken);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    private void setUserIdToAttribute(HttpServletRequest request, String userId) {
        request.setAttribute(USER_ID_ATTRIBUTE_KEY,userId);
    }
    private void setRefreshTokenToAttribute(HttpServletRequest request, String refreshToken) {
        request.setAttribute(REFRESH_TOKEN_HEADER,refreshToken);
    }
    private void checkTokenValid(String refreshToken, String userId) {
        tokenUtil.validRefreshToken(userId, refreshToken);
    }

    protected String getTokenFromHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }
    private void checkRefreshTokenExist(String token) {
        if (isEmpty(token)) {
            throw new RefreshTokenRequiredException(this.uri);
        }
    }
    private void checkAccessTokenExist(String token) {
        if (isEmpty(token)) {
            throw new AccessTokenRequiredException(this.uri);
        }
    }



}
