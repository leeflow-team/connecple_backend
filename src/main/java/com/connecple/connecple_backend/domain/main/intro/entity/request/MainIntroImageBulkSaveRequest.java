package com.connecple.connecple_backend.domain.main.intro.entity.request;

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
public class MainIntroImageBulkSaveRequest {
    private List<MultipartFile> images;
    private List<String> titles;
    private List<String> companies;
}