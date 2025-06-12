package com.connecple.connecple_backend.domain.faq.entity.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQAllResponse {
    private Long id;
    private String category;
    private String question;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
