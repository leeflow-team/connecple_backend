package com.connecple.connecple_backend.domain.main.intro.controller;

import com.connecple.connecple_backend.domain.AbstractWebTest;
import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQUpdateRequest;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

class FAQManagementControllerTest extends AbstractWebTest {

        @Test
        void createFAQTest() {
                FAQCreateRequest request = new FAQCreateRequest(
                                "위드프로젝트",
                                "서비스 신청은 어떻게 하나요?",
                                "홈페이지에서 신청 버튼을 누르면 됩니다.",
                                true,
                                false);

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

        /**
         * FAQ 상세 조회 테스트
         */
        @Test
        void getFAQDetailTest() {
                // given: 먼저 등록되어 있다고 가정하는 FAQ ID
                Long faqId = 1L;

                // when
                WebTestClient.ResponseSpec response = authenticatedGet("/admin/faqs/" + faqId)
                                .exchange()
                                .expectStatus().isOk();

                // then
                SuccessResponse<FAQDetailResponse> result = response.expectBody(
                                new ParameterizedTypeReference<SuccessResponse<FAQDetailResponse>>() {
                                })
                                .returnResult()
                                .getResponseBody();
        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        static class FAQDetailResponse {
                private Long id;
                private String category;
                private String question;
                private String answer;
                private Boolean isActive;
        }

        /**
         * FAQ 수정 테스트
         */
        @Test
        void updateFAQTest() {
                // given: 수정할 대상 ID는 테스트용으로 1L로 가정
                Long faqId = 1L;

                FAQUpdateRequest request = new FAQUpdateRequest(
                                "수정된 카테고리",
                                "수정된 질문입니다.",
                                "수정된 답변입니다.",
                                false,
                                null // 파일은 null로 설정
                );

                // when
                WebTestClient.ResponseSpec response = authenticatedPatch("/admin/faqs/" + faqId, request)
                                .exchange()
                                .expectStatus().isOk();

                // then
                SuccessResponse<Void> result = response.expectBody(
                                new ParameterizedTypeReference<SuccessResponse<Void>>() {
                                })
                                .returnResult()
                                .getResponseBody();

                System.out.println("updateFAQTest result = " + result);
        }

}
