package com.connecple.connecple_backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleMemberException(BaseException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity
                .status(e.getErrorCode())
                .body(exceptionResponse);
    }
}