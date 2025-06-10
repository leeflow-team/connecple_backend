package com.connecple.connecple_backend.domain.faq.service;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQDetailResponse;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQCreateRequest;
import com.connecple.connecple_backend.domain.faq.repository.FAQManagementRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
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
}
