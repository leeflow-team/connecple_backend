package com.connecple.connecple_backend.domain.story.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "story_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Story extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sortOrder;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Builder
    public Story(Long sortOrder, String imagePath, String title, String content) {
        this.sortOrder = sortOrder;
        this.imagePath = imagePath;
        this.title = title;
        this.content = content;
    }
}
