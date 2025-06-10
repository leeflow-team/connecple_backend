package com.connecple.connecple_backend.domain.main.intro.controller;

import com.connecple.connecple_backend.domain.AbstractWebTest;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

/**
 * 메인 통계 수치 관리 API 테스트
 * - 모든 요청은 로그인 인증이 필요함
 */
class MainStatsManagementControllerTest extends AbstractWebTest {

    /**
     * 메인 통계 수치 저장 테스트
     */
    @Test
    void updateMainStatsTest() {
        List<MainStatsUpdateRequest> request = List.of(
                new MainStatsUpdateRequest("커넥플과 함께 성장한 고객사", new BigDecimal("21"), "개", 1L),
                new MainStatsUpdateRequest("커넥플과 함께 성공한 프로젝트", new BigDecimal("34"), "개", 2L),
                new MainStatsUpdateRequest("커넥플과 함께 기대한 사업 만족도", new BigDecimal("97.3"), "%", 3L),
                new MainStatsUpdateRequest("커넥플과 함께 걸어온 경력보유여성", new BigDecimal("575"), "명", 4L),
                new MainStatsUpdateRequest("커넥플과 함께 재도약에 성공한 경력보유여성", new BigDecimal("81"), "명", 5L)
        );

        WebTestClient.ResponseSpec response = authenticatedPost("/admin/stats", request)
                .exchange()
                .expectStatus().isOk();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("update-main-stats result = " + result);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MainStatsUpdateRequest {
        private String statsName;
        private BigDecimal statistic;
        private String unit;
        private Long sortOrder;
    }


    /**
     * 메인 통계 수치 조회 테스트
     */
    @Test
    void getMainStatsTest() {
        WebTestClient.ResponseSpec response = authenticatedGet("/admin/stats")
                .exchange()
                .expectStatus().isOk();

        var result = response.expectBody(
                        new ParameterizedTypeReference<SuccessResponse<List<MainStatsResponse>>>() {})
                .returnResult()
                .getResponseBody();

        assert result != null;
        List<MainStatsResponse> statsList = result.getData();

        System.out.println("size = " + statsList.size());
        for (int i = 0; i < statsList.size(); i++) {
            MainStatsResponse stats = statsList.get(i);
            if (stats == null) {
                System.out.println("stats[" + i + "] = null");
            } else {
                System.out.println("stats[" + i + "] = " + stats);
            }
        }

        assert statsList.size() == 5;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MainStatsResponse {
        private String statsName;
        private BigDecimal statistic;
        private String unit;
        private Long sortOrder;
    }
}
