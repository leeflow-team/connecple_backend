package com.connecple.connecple_backend.domain.story.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryBulkSaveRequest {
    private List<MultipartFile> images;
    private List<String> titles;
    private List<String> contents;
}
