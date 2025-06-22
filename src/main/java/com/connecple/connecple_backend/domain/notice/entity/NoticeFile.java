package com.connecple.connecple_backend.domain.notice.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeFileDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notice_file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_management_id", nullable = false)
    private NoticeManagement noticeManagement;

    public NoticeFileDto toDto() {
        return NoticeFileDto.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .filePath(this.filePath)
                .fileSize(this.fileSize)
                .fileType(this.fileType)
                .build();
    }
}