package com.connecple.connecple_backend.domain.faq.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faq_files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "faqManagement")
@Builder
public class FAQFile extends BaseTimeEntity {

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
    @JoinColumn(name = "faq_id", nullable = false)
    private FAQManagement faqManagement;

    @Builder
    public FAQFile(String originalFileName, String storedFileName, String filePath,
            Long fileSize, String fileType, FAQManagement faqManagement) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.faqManagement = faqManagement;
    }
}