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
public class FAQUpdateRequest {

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "질문은 필수입니다.")
    private String question;

    @NotBlank(message = "답변은 필수입니다.")
    private String answer;

    @NotNull(message = "활성화 여부는 필수입니다.")
    private Boolean isActive;

    // 새로 업로드할 파일들 (기존 파일은 모두 삭제되고 이것들로 교체)
    private List<MultipartFile> files;
}
