package com.connecple.connecple_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "connecple_intro_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ConnecpleIntroManagement extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String historyYear;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long sortOrder;

    @Builder
    public ConnecpleIntroManagement(String historyYear, String content, Long sortOrder) {
        this.historyYear = historyYear;
        this.content = content;
        this.sortOrder = sortOrder;
    }
}
