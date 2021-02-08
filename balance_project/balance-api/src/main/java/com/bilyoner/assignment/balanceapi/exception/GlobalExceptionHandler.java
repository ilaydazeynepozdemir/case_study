package com.bilyoner.assignment.balanceapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleGenericException(Exception e) {
        logger.error(e);
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();
        return bodyBuilder.body(e.getMessage());
    }
}