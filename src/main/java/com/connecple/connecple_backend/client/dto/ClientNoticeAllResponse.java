package com.connecple.connecple_backend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ClientNoticeAllResponse {
    private Long id;
    private String category;
    private String title;
    private LocalDateTime createdAt;
    private int fileCount;
}