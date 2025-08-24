package com.connecple.connecple_backend.domain.story.repository;

import com.connecple.connecple_backend.domain.story.dto.StoryDto;

import java.util.List;

public interface StoryRepositoryQdsl {
    public List<StoryDto> getStoryList();
}
