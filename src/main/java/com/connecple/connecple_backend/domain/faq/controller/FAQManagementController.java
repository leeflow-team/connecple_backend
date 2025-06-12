package com.connecple.connecple_backend.domain.faq.controller;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

import com.connecple.connecple_backend.domain.faq.entity.dto.FAQDetailResponse;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQCreateRequest;
import com.connecple.connecple_backend.domain.faq.service.FAQManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/faqs")
public class FAQManagementController {

    private final FAQManagementService faqManagementService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createFAQ(HttpSession session, @RequestBody @Valid FAQCreateRequest request) {
        checkAdmin(session);
        faqManagementService.createFAQ(request);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<SuccessResponse<FAQDetailResponse>> getFAQDetail(HttpSession session, @PathVariable Long faqId) {
        checkAdmin(session);
        FAQDetailResponse response = faqManagementService.getFAQById(faqId);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}
