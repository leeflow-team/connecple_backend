package com.connecple.connecple_backend.client.dto;


import com.connecple.connecple_backend.domain.program.dto.ProgramDto;
import com.connecple.connecple_backend.domain.program.dto.ProgramFileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientProgramResponse {
    private Long id;
    private Long sortOrder;
    private String level;
    private String content1;
    private String content2;
    private String content3;
    private String imagePath;
    private List<ProgramFileDto> programFiles;

    public ClientProgramResponse(ProgramDto program, List<ProgramFileDto> programFiles) {
        this.id = program.getId();
        this.sortOrder = program.getSortOrder();
        this.level = program.getLevel();
        this.content1 = program.getContent1();
        this.content2 = program.getContent2();
        this.content3 = program.getContent3();
        this.imagePath = program.getImagePath();
        this.programFiles = programFiles;
    }
}
