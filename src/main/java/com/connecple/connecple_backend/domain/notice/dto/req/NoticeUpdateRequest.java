package com.connecple.connecple_backend.domain.notice.dto.req;

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
public class NoticeUpdateRequest {

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "활성화 여부는 필수입니다.")
    private Boolean isActive;

    // 삭제할 파일들의 ID 목록
    private List<Long> deleteFileIds;

    // 새로 업로드할 파일들
    private List<MultipartFile> files;
}