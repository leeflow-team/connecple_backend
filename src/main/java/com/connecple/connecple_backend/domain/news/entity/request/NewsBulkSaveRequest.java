package com.connecple.connecple_backend.domain.news.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewsBulkSaveRequest {
    private List<MultipartFile> images;
    private List<String> titles;
    private List<String> contents;
    private List<String> newsUrlList;
}
