package com.connecple.connecple_backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private Integer errorCode;
    private String message;

    public ExceptionResponse(BaseException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
    }
}
