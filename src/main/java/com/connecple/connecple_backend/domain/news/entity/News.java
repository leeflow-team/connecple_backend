package com.connecple.connecple_backend.domain.news.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class News extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private Long sortOrder;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String newsUrl;

    @Builder
    public News(String imagePath, Long sortOrder, String title, String content, String newsUrl) {
        this.imagePath = imagePath;
        this.sortOrder = sortOrder;
        this.title = title;
        this.content = content;
        this.newsUrl = newsUrl;
    }
}
