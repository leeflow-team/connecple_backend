package com.connecple.connecple_backend.domain.link.entity.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainLinkUpdateRequest {
    @NotBlank(message = "타이틀은 필수입니다.")
    private String title;

    @NotBlank(message = "링크는 필수입니다.")
    private String linkPath;
}
