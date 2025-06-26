package com.connecple.connecple_backend.domain.faq.controller;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

import com.connecple.connecple_backend.domain.faq.entity.dto.FAQDetailResponse;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQListResponse;
import com.connecple.connecple_backend.domain.faq.service.FAQManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/faqs")
public class FAQManagementController {

    private final FAQManagementService faqManagementService;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<SuccessResponse<Void>> createFAQ(
            HttpSession session,
            @RequestParam("category") String category,
            @RequestParam("question") String question,
            @RequestParam("answer") String answer,
            @RequestParam("isActive") Boolean isActive,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        checkAdmin(session);

        faqManagementService.createFAQ(category, question, answer, isActive, files);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<SuccessResponse<FAQDetailResponse>> getFAQDetail(HttpSession session,
            @PathVariable Long faqId) {
        checkAdmin(session);
        FAQDetailResponse response = faqManagementService.getFAQById(faqId);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @PatchMapping(value = "/{faqId}", consumes = { "multipart/form-data" })
    public ResponseEntity<SuccessResponse<Void>> updateFAQ(
            HttpSession session,
            @PathVariable Long faqId,
            @RequestParam("category") String category,
            @RequestParam("question") String question,
            @RequestParam("answer") String answer,
            @RequestParam("isActive") Boolean isActive,
            @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        checkAdmin(session);

        faqManagementService.updateFAQ(faqId, category, question, answer, isActive, deleteFileIds, files);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @DeleteMapping("/{id}")
    @Description("FAQ 삭제")
    public ResponseEntity<SuccessResponse<Void>> deleteFAQ(HttpSession session,
            @PathVariable("id") Long id) {
        checkAdmin(session);
        faqManagementService.deleteFAQ(id);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @Description("FAQ 전체 조회 (다중 카테고리 필터링 포함)")
    @GetMapping
    public ResponseEntity<SuccessResponse<FAQListResponse>> readAllFAQs(HttpSession session,
            @RequestParam(name = "category", required = false) List<String> categories,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        checkAdmin(session);
        return ResponseEntity.ok().body(SuccessResponse.success(
                faqManagementService.readAllFAQ(categories, page, size, sortBy)));
    }

    @Description("FAQ 키워드 기반 검색")
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<FAQListResponse>> searchFAQ(
            HttpSession session,
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "category", required = false) List<String> categories,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

        checkAdmin(session);
        FAQListResponse response = faqManagementService.searchFAQ(keyword, categories, page, size, sortBy);
        return ResponseEntity.ok().body(SuccessResponse.success(response));
    }

}
