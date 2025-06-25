package com.connecple.connecple_backend.domain.faq.entity.dto;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    private List<FAQFileDto> files;

    public static FAQDetailResponse fromEntity(FAQManagement faq) {
        List<FAQFileDto> fileDtos = faq.getFiles().stream()
                .map(FAQFileDto::fromEntity)
                .collect(Collectors.toList());

        return new FAQDetailResponse(
                faq.getId(),
                faq.getCategory(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getIsActive(),
                faq.getCreatedAt(),
                fileDtos);
    }
}
