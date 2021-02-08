package com.bilyoner.assignment.couponapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleGenericException(Exception e) {
        logger.error(e);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();
        return bodyBuilder.body(e.getMessage());
    }
}