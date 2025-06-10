package com.connecple.connecple_backend.domain.main.link.dto;

import com.connecple.connecple_backend.domain.main.link.entity.MainLinkManagement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainLinkResponseDto {
    private String title;
    private String linkPath;

    public static MainLinkResponseDto fromEntity(MainLinkManagement mainLinkManagement) {
        return new MainLinkResponseDto(mainLinkManagement.getTitle(), mainLinkManagement.getLinkPath());
    }
}
