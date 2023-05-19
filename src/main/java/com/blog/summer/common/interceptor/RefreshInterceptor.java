package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.RefreshTokenRequiredException;
import com.blog.summer.common.util.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.summer.common.util.JwtUtil.REFRESH_TOKEN_HEADER;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class RefreshInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
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
        this.setToken(this.getTokenFromHeader(request, REFRESH_TOKEN_HEADER));
        checkTokenExist();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    protected String getTokenFromHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    protected void checkTokenExist() {
        if (isEmpty(this.token)) {
            throw new RefreshTokenRequiredException(this.uri);
        }
    }
}
