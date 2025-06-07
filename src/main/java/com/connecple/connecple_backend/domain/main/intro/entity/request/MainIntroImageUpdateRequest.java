package com.connecple.connecple_backend.domain.main.intro.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainIntroImageUpdateRequest {
    private String imagePath;
    private String title;
    private String company;
}
