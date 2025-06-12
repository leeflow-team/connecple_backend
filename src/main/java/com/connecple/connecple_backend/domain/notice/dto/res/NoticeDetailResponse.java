package com.connecple.connecple_backend.domain.notice.dto.res;

import com.connecple.connecple_backend.domain.notice.entity.NoticeManagement;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeDetailResponse {
  private Long id;
  private String category;
  private String title;
  private String content;
  private Boolean isActive;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static NoticeDetailResponse fromEntity(NoticeManagement noticeManagement){
    return new NoticeDetailResponse(
        noticeManagement.getId(),
        noticeManagement.getCategory(),
        noticeManagement.getTitle(),
        noticeManagement.getContent(),
        noticeManagement.getIsActive(),
        noticeManagement.getIsDeleted(),
        noticeManagement.getDeletedAt(),
        noticeManagement.getCreatedAt(),
        noticeManagement.getUpdatedAt()
    );
  }


}
