package com.connecple.connecple_backend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClientNoticeListResponse {
    private List<ClientNoticeAllResponse> notices;
    private long totalCount;
    private int page;
    private int size;
    private int totalPages;
}