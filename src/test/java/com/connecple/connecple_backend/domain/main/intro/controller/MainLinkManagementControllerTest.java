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

    /**
     * 정상 수정 테스트
     */
    @Test
    void updateMainLinkSuccessTest() {
        MainLinkUpdateRequest request = new MainLinkUpdateRequest(
                "위드프로젝트 메인 링크 설정",
                "https://connecple.com/updated"
        );

        WebTestClient.ResponseSpec response = authenticatedPatch("/admin/main-links", request)
                .exchange()
                .expectStatus().isOk();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("update success = " + result);
    }

    /**
     * 존재하지 않는 title로 수정 시 예외 테스트
     */
    @Test
    void updateMainLinkFailWhenTitleNotFound() {
        MainLinkUpdateRequest request = new MainLinkUpdateRequest(
                "존재하지 않는 타이틀",
                "https://invalid-link.com"
        );

        WebTestClient.ResponseSpec response = authenticatedPatch("/admin/main-links", request)
                .exchange()
                .expectStatus().isBadRequest();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("update fail (not found) = " + result);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MainLinkUpdateRequest {
        private String title;
        private String linkPath;
    }
}
