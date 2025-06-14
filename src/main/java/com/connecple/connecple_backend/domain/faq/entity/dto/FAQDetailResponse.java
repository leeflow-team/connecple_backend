package com.connecple.connecple_backend.domain.faq.entity.dto;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQDetailResponse {
    private Long id;
    private String category;
    private String question;
    private String answer;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public static FAQDetailResponse fromEntity(FAQManagement faq) {
        return new FAQDetailResponse(
                faq.getId(),
                faq.getCategory(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getIsActive(),
                faq.getCreatedAt()
        );
    }
}

