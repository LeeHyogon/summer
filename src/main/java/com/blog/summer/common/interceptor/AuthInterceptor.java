package com.blog.summer.common.interceptor;


import com.blog.summer.common.exception.ExpiredTokenException;
import com.blog.summer.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RequiredArgsConstructor
abstract public class AuthInterceptor implements HandlerInterceptor {

    private static final String USER_ID_ATTRIBUTE_KEY = "userId";

    private final TokenUtil tokenUtil;

    @Getter
    @Setter
    protected String token;

    @Getter
    @Setter
    protected String uri;

    @Setter
    protected String tokenType;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.setUri(request.getRequestURI());
        checkTokenExist();
        checkTokenExpired();
        setUserIdToAttribute(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    abstract protected void checkTokenExist();

    protected String getTokenFromHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    private void checkTokenExpired() {
        if (tokenUtil.isExpired(this.token)) {
            throw new ExpiredTokenException();
        }
    }

    private void setUserIdToAttribute(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }
}