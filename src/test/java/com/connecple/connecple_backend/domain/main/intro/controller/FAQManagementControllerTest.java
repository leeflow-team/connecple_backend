package com.connecple.connecple_backend.domain.main.intro.controller;

import com.connecple.connecple_backend.domain.AbstractWebTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

class FAQManagementControllerTest extends AbstractWebTest {

    @Test
    void createFAQTest() {
        FAQCreateRequest request = new FAQCreateRequest(
                "위드프로젝트",
                "서비스 신청은 어떻게 하나요?",
                "홈페이지에서 신청 버튼을 누르면 됩니다.",
                true,
                false
        );

        WebTestClient.ResponseSpec response = authenticatedPost("/admin/faqs", request)
                .exchange()
                .expectStatus().isOk();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("createFAQTest result = " + result);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class FAQCreateRequest {
        private String category;
        private String question;
        private String answer;
        private Boolean isActive;
        private Boolean isDeleted = false;
    }
}
