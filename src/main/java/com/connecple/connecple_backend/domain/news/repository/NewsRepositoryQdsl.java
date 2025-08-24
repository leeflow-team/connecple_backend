package com.connecple.connecple_backend.domain.news.repository;

import com.connecple.connecple_backend.domain.news.dto.NewsDto;

import java.util.List;

public interface NewsRepositoryQdsl {
    public List<NewsDto> getNews();
}
