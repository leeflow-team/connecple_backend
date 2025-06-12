package com.connecple.connecple_backend.domain.faq.service;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.QFAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQAllResponse;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQDetailResponse;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQListResponse;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQCreateRequest;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQUpdateRequest;
import com.connecple.connecple_backend.domain.faq.repository.FAQManagementRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import com.querydsl.core.BooleanBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public FAQListResponse readAllFAQ(int page, int size, String sortBy) {
        int pageSize = switch (size) {
            case 30 -> 30;
            case 50 -> 50;
            default -> 10;
        };

        Sort sort = Sort.by(
                "createdAt".equals(sortBy) ? "createdAt" : "updatedAt"
        ).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<FAQManagement> pageResult = faqManagementRepository.findAllByIsDeletedIsFalse(pageable);

        List<FAQAllResponse> resultList = pageResult.getContent().stream()
                .map(faq -> new FAQAllResponse(
                        faq.getId(),
                        faq.getCategory(),
                        faq.getQuestion(),
                        faq.getIsActive(),
                        faq.getCreatedAt()
                ))
                .toList();

        return new FAQListResponse(
                resultList,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public FAQListResponse searchFAQ(String keyword, int page, int size, String sortBy) {
        int pageSize = switch (size) {
            case 30 -> 30;
            case 50 -> 50;
            default -> 10;
        };

        Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        QFAQManagement faq = QFAQManagement.fAQManagement;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(faq.isDeleted.isFalse());

        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(
                    faq.category.containsIgnoreCase(keyword)
                            .or(faq.question.containsIgnoreCase(keyword))
                            .or(faq.answer.containsIgnoreCase(keyword))
            );
        }

        Page<FAQManagement> faqPage = faqManagementRepository.findAllWithQueryDsl(builder, pageable, sortBy);

        List<FAQAllResponse> responseList = faqPage.getContent().stream()
                .map(f -> new FAQAllResponse(
                        f.getId(),
                        f.getCategory(),
                        f.getQuestion(),
                        f.getIsActive(),
                        f.getCreatedAt()
                ))
                .toList();

        return new FAQListResponse(
                responseList,
                faqPage.getTotalElements(),
                faqPage.getNumber(),
                faqPage.getSize(),
                faqPage.getTotalPages()
        );
    }


}
