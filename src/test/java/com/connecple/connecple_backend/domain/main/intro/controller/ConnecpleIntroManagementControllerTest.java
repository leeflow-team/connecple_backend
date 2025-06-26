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
 * 커넥플 연혁 등록 API 테스트
 */
class ConnecpleIntroManagementControllerTest extends AbstractWebTest {

    /**
     * 연혁 정상 등록 테스트
     */
    @Test
    void saveAllHistories_success() {
        List<ConnecpleHistorySaveRequest> historyList = List.of(
                new ConnecpleHistorySaveRequest("2025", "3차 대회\n2차 대회\n1차 대회"),
                new ConnecpleHistorySaveRequest("2021", "공동 경진대회 3차\n공동 경진대회 2차\n공동 경진대회 1차")
        );
        ConnecpleHistoryBulkSaveRequest request = new ConnecpleHistoryBulkSaveRequest(historyList);

        WebTestClient.ResponseSpec response = authenticatedPost("/admin/intro/history", request)
                .exchange()
                .expectStatus().isOk();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("saveAllHistories_success result = " + result);
    }

    /**
     * 연혁 리스트가 빈 경우 (전체 삭제만 수행)
     */
    @Test
    void saveAllHistories_emptyList() {
        ConnecpleHistoryBulkSaveRequest request = new ConnecpleHistoryBulkSaveRequest(List.of());

        WebTestClient.ResponseSpec response = authenticatedPost("/admin/intro/history", request)
                .exchange()
                .expectStatus().isOk();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("saveAllHistories_emptyList result = " + result);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ConnecpleHistorySaveRequest {
        private String historyYear;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ConnecpleHistoryBulkSaveRequest {
        private List<ConnecpleHistorySaveRequest> historyList;
    }


    /**
     * 연혁 전체 조회 테스트
     */
    @Test
    void getAllIntroHistoriesTest() {
        WebTestClient.ResponseSpec response = authenticatedGet("/admin/intro/history")
                .exchange()
                .expectStatus().isOk();

        SuccessResponse<List<ConnecpleHistoryResponse>> result = response.expectBody(
                        new ParameterizedTypeReference<SuccessResponse<List<ConnecpleHistoryResponse>>>() {})
                .returnResult()
                .getResponseBody();

        List<ConnecpleHistoryResponse> historyList = result.getData();
        System.out.println("총 연도 수: " + historyList.size());

        for (ConnecpleHistoryResponse history : historyList) {
            System.out.println("연도: " + history.getHistoryYear());
            System.out.println("내용: \n" + history.getContent());
        }
    }

    // 내부 응답 DTO 정의
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ConnecpleHistoryResponse {
        private String historyYear;
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class SuccessResponse<T> {
        private T data;
    }
}
