package com.connecple.connecple_backend.client.controller;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

import com.connecple.connecple_backend.client.dto.ClientFAQDetailResponse;
import com.connecple.connecple_backend.client.dto.ClientFAQListResponse;
import com.connecple.connecple_backend.client.dto.HomeResponse;
import com.connecple.connecple_backend.client.service.ClientService;
import com.connecple.connecple_backend.domain.connecple.intro.entity.dto.ConnecpleHistoryResponse;
import com.connecple.connecple_backend.domain.connecple.intro.service.ConnecpleIntroManagementService;
import com.connecple.connecple_backend.domain.faq.service.FAQManagementService;
import com.connecple.connecple_backend.domain.link.entity.dto.MainLinkResponseDto;
import com.connecple.connecple_backend.domain.link.service.MainLinkManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ClientController {

    private final ClientService clientService;
    private final MainLinkManagementService mainLinkManagementService;
    private final ConnecpleIntroManagementService connecpleIntroManagementService;
    private final FAQManagementService faqManagementService;

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> getHomeData() {
        HomeResponse homeData = clientService.getHomeData();
        return ResponseEntity.ok(homeData);
    }

    @GetMapping("/links")
    public ResponseEntity<List<MainLinkResponseDto>> getMainLinks() {
        return ResponseEntity.ok(mainLinkManagementService.getMainLinks());
    }

    /**
     * 모든 회사 연혁 조회 (historyYear 기준 내림차순, content는 줄바꿈으로 연결)
     */
    @GetMapping("/history")
    public ResponseEntity<List<ConnecpleHistoryResponse>> getAllIntroHistories() {
        List<ConnecpleHistoryResponse> response = connecpleIntroManagementService.getAllIntros();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/faqs/{faqId}")
    public ResponseEntity<ClientFAQDetailResponse> getFAQDetail(@PathVariable Long faqId) {
        ClientFAQDetailResponse response = faqManagementService.getClientFAQById(faqId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/faqs")
    public ResponseEntity<ClientFAQListResponse> readAllFAQs(
            @RequestParam(name = "category", required = false) List<String> categories,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ClientFAQListResponse response = faqManagementService.readAllClientFAQ(categories, page, size, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/faqs/search")
    public ResponseEntity<ClientFAQListResponse> searchFAQ(
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "category", required = false) List<String> categories,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        ClientFAQListResponse response = faqManagementService.searchClientFAQ(keyword, categories, page, size, sortBy);
        return ResponseEntity.ok(response);
    }
}