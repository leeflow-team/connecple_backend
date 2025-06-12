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

  public NoticeAllResponse toResponse(NoticeManagement noticeManagement){
    return new NoticeAllResponse(noticeManagement.getId(), noticeManagement.getCategory() ,noticeManagement.getTitle(), noticeManagement.getIsActive(), noticeManagement.getCreatedAt());
  }
}
