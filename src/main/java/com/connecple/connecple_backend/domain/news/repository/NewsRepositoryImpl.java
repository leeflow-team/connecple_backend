package com.connecple.connecple_backend.domain.news.repository;

import com.connecple.connecple_backend.domain.news.dto.NewsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.connecple.connecple_backend.domain.news.entity.QNews.news;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NewsRepositoryImpl implements NewsRepositoryQdsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NewsDto> getNews() {
        return queryFactory.select(Projections.constructor(NewsDto.class,news))
                .from(news)
                .orderBy(news.sortOrder.asc())
                .fetch();
    }

}
