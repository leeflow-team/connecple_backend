package com.connecple.connecple_backend.domain.notice.dto.req;

import jakarta.persistence.Column;
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
public class NoticeCreateRequest {
  @NotBlank(message = "카테고리는 필수입니다.")
  private String category;

  @NotBlank(message = "제목 작성은 필수입니다.")
  private String title;

  @NotBlank(message = "내용 작성은 필수입니다.")
  private String content;

  @NotNull(message = "상태 설정은 필수입니다.")
  private Boolean isActive;

  // 첨부 파일들
  private List<MultipartFile> files;
}
