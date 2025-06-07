package com.connecple.connecple_backend.domain.main.intro.controller;

import com.connecple.connecple_backend.domain.AbstractWebTest;
import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/**
 * 메인 소개 이미지 CRUD API 테스트
 * - 모든 요청은 로그인 인증이 필요함
 */
class MainIntroImageControllerTest extends AbstractWebTest {

    /**
     * 메인 소개 이미지 생성 테스트
     */
    @Test
    void createMainIntroImageTest() {
        // 로그인 후 세션 쿠키 획득
        String sessionId = loginAndGetSession();

        MainIntroImageCreateRequest request = new MainIntroImageCreateRequest(
                "path", 1L, "title", "company"
        );

        WebTestClient.ResponseSpec response = client.post()
                .uri("/admin/main-intro-images")
                .cookie("JSESSIONID", sessionId) // 세션 쿠키 추가
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        String result = response.expectBody(String.class).returnResult().getResponseBody();
        System.out.println("main-intro-images result = " + result);
    }

    /**
     * 메인 소개 이미지 조회 테스트
     */
    @Test
    void readMainIntroImageTest() {

        WebTestClient.ResponseSpec response = client.get()
                .uri("/admin/main-intro-images")
                .exchange()
                .expectStatus().isCreated();

        MainIntroImageDto result = response.expectBody(MainIntroImageDto.class).returnResult().getResponseBody();
        System.out.println("main-intro-images result = " + result);
    }

    /**
     * 메인 소개 이미지 수정 테스트
     */
    @Test
    void updateMainIntroImageTest() {
        MainIntroImageUpdateRequest request = new MainIntroImageUpdateRequest("path", "title", "company");
        long updateId = 1L;

        WebTestClient.ResponseSpec response = client.patch()
                .uri("/admin/main-intro-images/" + updateId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        MainIntroImageDto dto = response.expectBody(MainIntroImageDto.class).returnResult().getResponseBody();
        System.out.println("Updated dto = " + dto);
    }

    /**
     * 메인 소개 이미지 삭제 테스트
     */

    @Test
    void deleteMainIntroImageTest() {
        long deleteId = 1L;

        WebTestClient.ResponseSpec response = client.delete()
                .uri("/admin/main-intro-images/" + deleteId)
                .exchange()
                .expectStatus().isNoContent();
    }
*/
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MainIntroImageCreateRequest {
        private String imagePath;
        private Long sortOrder;
        private String title;
        private String company;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class MainIntroImageUpdateRequest {
        private String imagePath;
        private String title;
        private String company;
    }
}