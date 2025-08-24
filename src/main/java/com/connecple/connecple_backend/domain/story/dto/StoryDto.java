package com.connecple.connecple_backend.domain.story.dto;

import com.connecple.connecple_backend.domain.story.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoryDto {
    private Long id;
    private Long sortOrder;
    private String imagePath;
    private String title;
    private String content;

    public StoryDto(Story story) {
        this.id = story.getId();
        this.sortOrder = story.getSortOrder();
        this.imagePath = story.getImagePath();
        this.title = story.getTitle();
        this.content = story.getContent();
    }
}
