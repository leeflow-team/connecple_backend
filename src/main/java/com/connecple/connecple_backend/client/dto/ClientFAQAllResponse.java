package com.connecple.connecple_backend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ClientFAQAllResponse {
    private Long id;
    private String category;
    private String question;
    private LocalDateTime createdAt;
    private Integer fileCount;
}