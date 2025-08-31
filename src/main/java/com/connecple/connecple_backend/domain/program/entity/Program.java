package com.connecple.connecple_backend.domain.program.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "program_management")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sortOrder;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String content1;

    @Column(nullable = false)
    private String content2;

    @Column(nullable = false)
    private String content3;

    @Column(nullable = false)
    private String imagePath;

    @Builder
    public Program(Long sortOrder, String level, String content1, String content2, String content3, String imagePath) {
        this.sortOrder = sortOrder;
        this.level = level;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
        this.imagePath = imagePath;
    }
}
