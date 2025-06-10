package com.connecple.connecple_backend.domain.main.stats.entity.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainStatsUpdateRequestList {

    @NotEmpty(message = "통계 수치 리스트는 비어있을 수 없습니다.")
    @Valid
    private List<MainStatsUpdateRequest> statsList;
}
