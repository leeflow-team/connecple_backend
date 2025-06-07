package com.connecple.connecple_backend.domain.main.intro.entity.dto;

import com.connecple.connecple_backend.domain.main.intro.entity.MainIntroImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainIntroImageDto {
    private Long id;
    private String imagePath;
    private Long sortOrder;
    private String title;
    private String company;

    public MainIntroImageDto(MainIntroImage mainIntroImage) {
        this.id = mainIntroImage.getId();
        this.imagePath = mainIntroImage.getImagePath();
        this.sortOrder = mainIntroImage.getSortOrder();
        this.title = mainIntroImage.getTitle();
        this.company = mainIntroImage.getCompany();
    }
}
