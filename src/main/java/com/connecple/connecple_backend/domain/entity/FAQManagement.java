package com.connecple.connecple_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "faq_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FAQManagement extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public FAQManagement(String category, String question, String answer, Boolean isActive) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.isActive = isActive;
        this.isDeleted = false;
    }
}
