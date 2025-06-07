package com.connecple.connecple_backend.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer errorCode;
    private final String message;

    public BaseException(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
