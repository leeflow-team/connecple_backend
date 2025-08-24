package com.connecple.connecple_backend.domain.news.dto;

import com.connecple.connecple_backend.domain.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewsDto {
    private Long id;
    private String imagePath;
    private Long sortOrder;
    private String title;
    private String content;
    private String newsUrl;

    public NewsDto(News news) {
        this.id = news.getId();
        this.imagePath = news.getImagePath();
        this.sortOrder = news.getSortOrder();
        this.title = news.getTitle();
        this.content = news.getContent();
        this.newsUrl = news.getNewsUrl();
    }
}
