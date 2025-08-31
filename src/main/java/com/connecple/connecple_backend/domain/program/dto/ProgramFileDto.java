package com.connecple.connecple_backend.domain.program.dto;

import com.connecple.connecple_backend.domain.program.entity.ProgramFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProgramFileDto {
    private Long id;
    private Long programId;
    private String imagePath;

    public ProgramFileDto(ProgramFile programFile) {
        this.id = programFile.getId();
        this.programId = programFile.getProgramId();
        this.imagePath = programFile.getImagePath();
    }
}
