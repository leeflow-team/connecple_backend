package com.connecple.connecple_backend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse("요청이 성공적으로 처리되었습니다.", data);
    }
}
