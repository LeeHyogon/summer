package com.blog.summer.advice;


import com.blog.summer.domain.badcomment.BadComment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

@ControllerAdvice
@RequiredArgsConstructor
public class TestResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return false;
        /*
        return returnType.getParameterType().equals(ResponseEntity.class)
                && returnType.getGenericParameterType() instanceof ParameterizedType
                && ((ParameterizedType) returnType.getGenericParameterType())
                .getActualTypeArguments()[0].equals(BadComment.class);

         */
    }
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        /*
        if (body instanceof ResponseEntity) {
            ResponseEntity<BadComment> responseEntity = (ResponseEntity<BadComment>) body;
            Object bodyObj = responseEntity.getBody();
            if (bodyObj instanceof String) {
                try {
                    BadComment o = (BadComment) objectMapper.readValue((String) bodyObj, returnType.getParameterType());
                    o.setBody("착한말쓰기!!");
                    return o;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
         */
        return body;
    }
}
