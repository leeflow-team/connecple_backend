package com.connecple.connecple_backend.client.dto;

import com.connecple.connecple_backend.domain.notice.dto.res.NoticeFileDto;
import com.connecple.connecple_backend.domain.notice.entity.NoticeManagement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ClientNoticeDetailResponse {
    private Long id;
    private String category;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<NoticeFileDto> files;

    public static ClientNoticeDetailResponse fromEntity(NoticeManagement noticeManagement) {
        List<NoticeFileDto> fileDtos = noticeManagement.getFiles().stream()
                .map(file -> file.toDto())
                .toList();

        return new ClientNoticeDetailResponse(
                noticeManagement.getId(),
                noticeManagement.getCategory(),
                noticeManagement.getTitle(),
                noticeManagement.getContent(),
                noticeManagement.getCreatedAt(),
                noticeManagement.getUpdatedAt(),
                fileDtos);
    }
}