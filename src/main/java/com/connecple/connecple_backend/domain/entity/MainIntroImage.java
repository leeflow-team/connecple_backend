package com.connecple.connecple_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "main_intro_images")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MainIntroImage extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private Long sortOrder;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Builder
    public MainIntroImage(String imagePath, Long sortOrder, String title, String company) {
        this.imagePath = imagePath;
        this.sortOrder = sortOrder;
        this.title = title;
        this.company = company;
    }
}
