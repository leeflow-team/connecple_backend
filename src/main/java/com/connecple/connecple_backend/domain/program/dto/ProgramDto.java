package com.connecple.connecple_backend.domain.program.dto;

import com.connecple.connecple_backend.domain.program.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProgramDto {
    private Long id;
    private String level;
    private Long sortOrder;
    private String content1;
    private String content2;
    private String content3;
    private String imagePath;

    public ProgramDto(Program program) {
        this.id = program.getId();
        this.level = program.getLevel();
        this.sortOrder = program.getSortOrder();
        this.content1 = program.getContent1();
        this.content2 = program.getContent2();
        this.content3 = program.getContent3();
        this.imagePath = program.getImagePath();
    }
}
