package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.AccessTokenRequiredException;
import com.blog.summer.common.util.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class AccessInterceptor extends AuthInterceptor {

    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    public AccessInterceptor(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        this.setToken(this.getTokenFromHeader(request, ACCESS_TOKEN_HEADER_NAME));

        return super.preHandle(request, response, handler);
    }

    @Override
    protected void checkTokenExist() {
        if (isEmpty(this.token) || !this.isValidType(ACCESS_TOKEN_TYPE)) {
            throw new AccessTokenRequiredException(this.uri);
        }
    }
}