package com.blog.summer.common.interceptor;


import com.blog.summer.common.exception.ExpiredTokenException;
import com.blog.summer.common.util.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RequiredArgsConstructor
abstract public class AuthInterceptor implements HandlerInterceptor {

    private static final String USER_ID_ATTRIBUTE_KEY = "userId";

    private final JwtUtil jwtUtil;

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
        setUserSeqToAttribute(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    abstract protected void checkTokenExist();

    protected String getTokenFromHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    protected boolean isValidType(String tokenType) {
        return jwtUtil.getTypeFromToken(this.token).equals(tokenType);
    }

    private void checkTokenExpired() {
        if (jwtUtil.isExpired(this.token)) {
            throw new ExpiredTokenException();
        }
    }

    private void setUserSeqToAttribute(HttpServletRequest request) {
        String userId = jwtUtil.getUserIdFromToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }
}