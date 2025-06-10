package com.connecple.connecple_backend.domain.faq.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FAQCreateRequest {

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "질문은 필수입니다.")
    private String question;

    @NotBlank(message = "답변은 필수입니다.")
    private String answer;

    @NotNull(message = "상태는 필수입니다.")
    private Boolean isActive;
}
