package com.connecple.connecple_backend.domain.notice.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import com.connecple.connecple_backend.domain.notice.dto.req.NoticeCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NoticeManagement extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    @Builder
    public NoticeManagement(String category, String title, String content, Boolean isActive) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.isActive = isActive;
        this.isDeleted = false;
    }

    public void updateNoticeManagement(NoticeCreateRequest request){
        this.category = request.getCategory();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.isActive = request.getIsActive();
    }

    public void deleteNoticeManagement(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
