package com.connecple.connecple_backend.domain.notice.dto.res;

import com.connecple.connecple_backend.domain.notice.entity.NoticeManagement;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeAllResponse {
  private Long id;
  private String category;
  private String title;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private int fileCount;

  // fileCount 없는 생성자 (기존 호환성 유지)
  public NoticeAllResponse(Long id, String category, String title, Boolean isActive, LocalDateTime createdAt) {
    this.id = id;
    this.category = category;
    this.title = title;
    this.isActive = isActive;
    this.createdAt = createdAt;
    this.fileCount = 0; // 기본값
  }

  public NoticeAllResponse toResponse(NoticeManagement noticeManagement) {
    return new NoticeAllResponse(noticeManagement.getId(), noticeManagement.getCategory(), noticeManagement.getTitle(),
        noticeManagement.getIsActive(), noticeManagement.getCreatedAt(), noticeManagement.getFiles().size());
  }
}
