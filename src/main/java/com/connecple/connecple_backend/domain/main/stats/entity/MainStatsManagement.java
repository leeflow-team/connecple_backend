package com.connecple.connecple_backend.domain.main.stats.entity;

import com.connecple.connecple_backend.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "main_stats_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MainStatsManagement extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String statsName;

    @Column(nullable = false)
    private BigDecimal statistic;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Long sortOrder;

    @Builder
    public MainStatsManagement(String statsName, BigDecimal statistic, String unit, Long sortOrder) {
        this.statsName = statsName;
        this.statistic = statistic;
        this.unit = unit;
        this.sortOrder = sortOrder;
    }
}
