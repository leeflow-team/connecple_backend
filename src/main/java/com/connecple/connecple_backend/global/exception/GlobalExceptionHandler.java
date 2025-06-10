package com.connecple.connecple_backend.global.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // DTO @Valid 실패 시 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(". "));

        return ResponseEntity.badRequest().body(new ExceptionResponse(400, message));
    }

}