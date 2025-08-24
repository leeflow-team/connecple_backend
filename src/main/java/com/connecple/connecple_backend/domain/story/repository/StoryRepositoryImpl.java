package com.connecple.connecple_backend.domain.story.repository;

import com.connecple.connecple_backend.domain.story.dto.StoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.connecple.connecple_backend.domain.story.entity.QStory.story;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StoryRepositoryImpl implements StoryRepositoryQdsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoryDto> getStoryList() {
        return queryFactory.select(Projections.constructor(StoryDto.class, story))
                .from(story)
                .orderBy(story.sortOrder.asc())
                .fetch();
    }
}
