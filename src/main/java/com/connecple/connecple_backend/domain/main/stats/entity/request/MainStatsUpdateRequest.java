package com.connecple.connecple_backend.domain.main.stats.entity.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainStatsUpdateRequest {

    @NotBlank(message = "통계 제목은 필수입니다.")
    private String statsName;

    @NotNull(message = "통계 수치는 필수입니다.")
    private BigDecimal statistic;

    @NotBlank(message = "통계 단위는 필수입니다.")
    private String unit;

    @NotNull(message = "정렬 순서는 필수입니다.")
    @Min(value = 1, message = "정렬 순서는 1 이상이어야 합니다.")
    @Max(value = 5, message = "정렬 순서는 5 이하여야 합니다.")
    private Long sortOrder;
}
