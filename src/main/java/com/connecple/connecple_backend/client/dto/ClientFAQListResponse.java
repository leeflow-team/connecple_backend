package com.connecple.connecple_backend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClientFAQListResponse {
    private List<ClientFAQAllResponse> faqs;
    private long totalCount;
    private int page;
    private int size;
    private int totalPages;
}