package com.connecple.connecple_backend.domain.main.intro.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainIntroImageCreateRequest {
    private String imagePath;
    private Long sortOrder;
    private String title;
    private String company;
}
