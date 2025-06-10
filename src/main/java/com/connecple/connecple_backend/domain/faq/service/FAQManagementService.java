package com.connecple.connecple_backend.domain.faq.service;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQCreateRequest;
import com.connecple.connecple_backend.domain.faq.repository.FAQManagementRepository;
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
}
