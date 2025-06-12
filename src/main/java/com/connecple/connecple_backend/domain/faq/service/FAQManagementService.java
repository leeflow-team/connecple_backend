package com.connecple.connecple_backend.domain.faq.service;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQDetailResponse;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQCreateRequest;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQUpdateRequest;
import com.connecple.connecple_backend.domain.faq.repository.FAQManagementRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FAQManagementService {

    private final FAQManagementRepository faqManagementRepository;

    @Transactional
    public void createFAQ(FAQCreateRequest request) {
        FAQManagement faq = FAQManagement.builder()
                .category(request.getCategory())
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .isActive(request.getIsActive())
                .isDeleted(false)
                .build();

        faqManagementRepository.save(faq);
    }

    public FAQDetailResponse getFAQById(Long id) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(404, "해당 FAQ가 존재하지 않습니다."));
        return FAQDetailResponse.fromEntity(faq);
    }

    @Transactional
    public void updateFAQ(Long id, FAQUpdateRequest request) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(404, "해당 FAQ가 존재하지 않습니다."));

        faq.update(request.getCategory(), request.getQuestion(), request.getAnswer(), request.getIsActive());
        faqManagementRepository.save(faq);
    }

    @Description("FAQ 삭제 - 이미 삭제된 항목은 또 삭제되지 않음")
    @Transactional
    public void deleteFAQ(Long id) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(400, "해당 FAQ를 찾을 수 없습니다."));

        faq.deleteFAQ();

        faqManagementRepository.save(faq);
    }


}
