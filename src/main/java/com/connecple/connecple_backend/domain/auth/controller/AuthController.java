package com.connecple.connecple_backend.domain.auth.controller;

import com.connecple.connecple_backend.domain.auth.model.LoginRequest;
import com.connecple.connecple_backend.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        if (authService.login(loginRequest)) {
            session.setAttribute("isAdmin", authService.login(loginRequest));
            return "success";
        }
        return "fail";
    }

    @GetMapping("/session-check")
    public ResponseEntity<Boolean> checkAdmin(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        return ResponseEntity.ok(isAdmin != null ? isAdmin : false);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
