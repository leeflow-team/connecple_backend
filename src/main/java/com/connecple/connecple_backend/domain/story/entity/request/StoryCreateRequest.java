package com.connecple.connecple_backend.domain.story.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryCreateRequest {
    private Long sortOrder;
    private String imagePath;
    private String title;
    private String content;
}
