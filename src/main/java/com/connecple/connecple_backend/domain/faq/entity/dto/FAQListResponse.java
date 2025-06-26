package com.connecple.connecple_backend.domain.faq.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQListResponse {
    private List<FAQAllResponse> faqs;
    private long totalCount;
    private int page;
    private int size;
    private int totalPages;
}
