package com.connecple.connecple_backend.domain.program.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "program_file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProgramFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long programId;

    @Column(nullable = false)
    private Long sortOrder;

    @Column(nullable = false)
    private String imagePath;

    @Builder
    public ProgramFile(Long programId, Long sortOrder, String imagePath) {
        this.programId = programId;
        this.sortOrder = sortOrder;
        this.imagePath = imagePath;
    }
}
