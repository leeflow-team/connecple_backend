package com.connecple.connecple_backend.domain.faq.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "faq_management", indexes = { @Index(name = "idx_faq_category", columnList = "category") })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FAQManagement extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "faqManagement", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<FAQFile> files = new ArrayList<>();

    @Builder
    public FAQManagement(String category, String question, String answer, Boolean isActive) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.isActive = isActive;
        this.isDeleted = false;
    }

    public void update(String category, String question, String answer, Boolean isActive) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.isActive = isActive;
    }

    public void deleteFAQ() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
