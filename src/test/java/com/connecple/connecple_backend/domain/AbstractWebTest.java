package com.connecple.connecple_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * 공통 테스트 베이스 클래스
 * - WebTestClient 초기화
 * - 로그인 후 JSESSIONID 발급 메서드
 * - 인증 요청 헬퍼 메서드 제공
 */
public class AbstractWebTest {

    protected WebTestClient client;

    // 테스트 실행 전 WebTestClient 초기화
    @BeforeEach
    void initClient() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    /**
     * 로그인 후 세션 쿠키(JSESSIONID) 반환
     */
    protected String loginAndGetSession() {
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

    // 인증된 POST 요청 헬퍼
    protected WebTestClient.RequestHeadersSpec<?> authenticatedPost(String uri, Object body) {
        return client.post()
                .uri(uri)
                .cookie("JSESSIONID", loginAndGetSession())
                .bodyValue(body);
    }

    // 인증된 GET 요청 헬퍼
    protected WebTestClient.RequestHeadersSpec<?> authenticatedGet(String uri) {
        return client.get()
                .uri(uri)
                .cookie("JSESSIONID", loginAndGetSession());
    }

    // 인증된 PATCH 요청 헬퍼
    protected WebTestClient.RequestHeadersSpec<?> authenticatedPatch(String uri, Object body) {
        return client.patch()
                .uri(uri)
                .cookie("JSESSIONID", loginAndGetSession())
                .bodyValue(body);
    }

    // 인증된 DELETE 요청 헬퍼
    protected WebTestClient.RequestHeadersSpec<?> authenticatedDelete(String uri) {
        return client.delete()
                .uri(uri)
                .cookie("JSESSIONID", loginAndGetSession());
    }


    // 로그인 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest {
        private String id;
        private String password;
    }
}
