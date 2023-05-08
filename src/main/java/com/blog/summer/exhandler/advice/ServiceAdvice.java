package com.blog.summer.exhandler.advice;

import com.blog.summer.exception.NotFoundException;
import com.blog.summer.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ServiceAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> notFoundExHandler(NotFoundException e){
        log.error("[notFoundExHandler] ex", e);
        ErrorResult result = new ErrorResult("NotFound", e.getMessage());
        return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
    }
}
