package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.AccessTokenRequiredException;
import com.blog.summer.common.exception.ExpiredTokenException;
import com.blog.summer.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ObjectUtils.isEmpty;
import static com.blog.summer.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;
import static com.blog.summer.common.util.TokenUtil.ACCESS_TOKEN_HEADER;

@Component
@RequiredArgsConstructor
public class AccessInterceptor implements HandlerInterceptor {


    private final TokenUtil tokenUtil;

    @Getter
    @Setter
    protected String token;

    @Getter
    @Setter
    protected String uri;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        this.setToken(this.getTokenFromHeader(request, ACCESS_TOKEN_HEADER));
        this.setUri(request.getRequestURI());
        checkTokenExist();
        checkTokenExpired();
        setUserIdToAttribute(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    protected String getTokenFromHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }
    protected void checkTokenExist() {
        if (isEmpty(this.token)) {
            throw new AccessTokenRequiredException(this.uri);
        }
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