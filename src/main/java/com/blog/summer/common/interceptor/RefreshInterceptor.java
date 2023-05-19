package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.RefreshTokenRequiredException;
import com.blog.summer.common.util.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.summer.common.util.JwtUtil.REFRESH_TOKEN_TYPE;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RefreshInterceptor extends AuthInterceptor {

    private static final String REFRESH_TOKEN_HEADER_NAME = "R-Authorization";

    public RefreshInterceptor(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        this.setToken(this.getTokenFromHeader(request, REFRESH_TOKEN_HEADER_NAME));

        return super.preHandle(request, response, handler);
    }

    @Override
    protected void checkTokenExist() {
        if (isEmpty(this.token) || !this.isValidType(REFRESH_TOKEN_TYPE)) {
            throw new RefreshTokenRequiredException(this.uri);
        }
    }
}
