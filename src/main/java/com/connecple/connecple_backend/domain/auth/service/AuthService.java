package com.connecple.connecple_backend.domain.auth.service;

import com.connecple.connecple_backend.domain.auth.model.LoginRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${admin.id}")
    private String adminId;

    @Value("${admin.password}")
    private String adminPassword;

    @Transactional
    public Boolean login(LoginRequest loginRequest) {
        if (adminId.equals(loginRequest.getId()) && adminPassword.equals(loginRequest.getPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
