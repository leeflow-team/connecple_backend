package com.connecple.connecple_backend.domain.notice.dto.res;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoticeListResponse {
  private List<NoticeAllResponse> notices;
  private long totalCount;
  private int page;
  private int size;
  private int totalPages;
}