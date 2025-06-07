package com.connecple.connecple_backend.domain.auth.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String id;
    private String password;
}
