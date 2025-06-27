package com.connecple.connecple_backend.client.dto;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQFileDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientFAQDetailResponse {
    private Long id;
    private String category;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
    private List<FAQFileDto> files;

    public static ClientFAQDetailResponse fromEntity(FAQManagement faq) {
        List<FAQFileDto> fileDtos = faq.getFiles().stream()
                .map(FAQFileDto::fromEntity)
                .collect(Collectors.toList());

        return new ClientFAQDetailResponse(
                faq.getId(),
                faq.getCategory(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getCreatedAt(),
                fileDtos);
    }
}
