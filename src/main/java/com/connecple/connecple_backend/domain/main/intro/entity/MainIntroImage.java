package com.connecple.connecple_backend.domain.main.intro.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

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

    public MainIntroImageDto updateEntity(MainIntroImageUpdateRequest request) {
        this.imagePath = request.getImagePath();
        this.title = request.getTitle();
        this.company = request.getCompany();

        return new MainIntroImageDto(this);
    }

    @Builder
    public MainIntroImage(String imagePath, Long sortOrder, String title, String company) {
        this.imagePath = imagePath;
        this.sortOrder = sortOrder;
        this.title = title;
        this.company = company;
    }
}
