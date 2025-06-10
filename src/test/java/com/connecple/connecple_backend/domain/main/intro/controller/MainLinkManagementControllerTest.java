package com.connecple.connecple_backend.domain.main.intro.controller;

import com.connecple.connecple_backend.domain.AbstractWebTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/**
 * 메인 링크 관리 API 테스트
 * - 모든 요청은 로그인 인증이 필요함
 */
class MainLinkManagementControllerTest extends AbstractWebTest {

    /**
     * 메인 링크 전체 조회 테스트
     */
    @Test
    void getMainLinksTest() {
        WebTestClient.ResponseSpec response = authenticatedGet("/admin/main-links")
                .exchange()
                .expectStatus().isOk();

        SuccessResponse<List<MainLinkResponse>> result = response.expectBody(
                        new ParameterizedTypeReference<SuccessResponse<List<MainLinkResponse>>>() {})
                .returnResult()
                .getResponseBody();

        assert result != null;
        List<MainLinkResponse> links = result.getData();

        System.out.println("Main links = " + links);
        assert links.size() == 4;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MainLinkResponse {
        private String title;
        private String linkPath;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class SuccessResponse<T> {
        private T data;
    }
}
