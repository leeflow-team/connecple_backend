package com.connecple.connecple_backend.domain.faq.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FAQCreateRequest {

    @NotBlank(message = "카테고리 설정은 필수입니다.")
    private String category;

    @NotBlank(message = "질문 작성은 필수입니다.")
    private String question;

    @NotBlank(message = "답변 작성은 필수입니다.")
    private String answer;

    @NotNull(message = "상태 설정은 필수입니다.")
    private Boolean isActive;

    // 파일은 선택사항 (null 가능)
    private List<MultipartFile> files;
}
