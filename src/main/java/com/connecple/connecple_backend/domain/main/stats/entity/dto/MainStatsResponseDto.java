package com.connecple.connecple_backend.domain.main.stats.entity.dto;

import com.connecple.connecple_backend.domain.main.stats.entity.MainStatsManagement;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainStatsResponseDto {
    private String statsName;
    private BigDecimal statistic;
    private String unit;
    private Long sortOrder;

    public static MainStatsResponseDto fromEntity(MainStatsManagement entity) {
        return new MainStatsResponseDto(
                entity.getStatsName(),
                entity.getStatistic(),
                entity.getUnit(),
                entity.getSortOrder()
        );
    }
}
