package com.connecple.connecple_backend.domain.connecple.intro.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnecpleHistorySaveRequest {
    @NotBlank(message = "연도는 필수입니다.")
    private String historyYear;

    @NotBlank(message = "내용은 필수입니다.")
    private String content; // 줄바꿈(\n)으로 구분된 항목들
}
