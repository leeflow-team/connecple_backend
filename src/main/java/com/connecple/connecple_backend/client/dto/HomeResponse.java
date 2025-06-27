package com.connecple.connecple_backend.client.dto;

import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.stats.entity.dto.MainStatsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponse {
    private List<MainIntroImageDto> introImages;
    private List<MainStatsResponseDto> stats;
}