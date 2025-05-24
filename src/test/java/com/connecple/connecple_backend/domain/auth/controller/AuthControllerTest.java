package com.connecple.connecple_backend.domain.auth.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

class AuthControllerTest {

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Test
    void loginTest() {
        LoginRequest request = new LoginRequest("admin", "1234");

        WebTestClient.ResponseSpec response = client.post()
                .uri("/admin/login")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("login result = " + result);
    }

    /**
     * 로그인 후 세션 쿠키(JSESSIONID)를 리턴하는 공통 메서드
     */
    private String loginAndGetSession() {
        LoginRequest request = new LoginRequest("admin", "1234");

        return client.post()
                .uri("/admin/login")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }

    /**
     * ✅ 실제 API 호출 테스트 (로그인 세션 활용)
     */
    @Test
    void sessionCheckTest() {
        String sessionId = loginAndGetSession();

        client.get()
                .uri("/admin/session-check")
                .cookie("JSESSIONID", sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .value(isAdmin -> System.out.println("로그인 세션 isAdmin = " + isAdmin));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest {
        private String id;
        private String password;
    }
}