package com.connecple.connecple_backend.domain.program.repository;

import com.connecple.connecple_backend.domain.program.dto.ProgramFileDto;

import java.util.List;

public interface ProgramFileRepositoryQdsl {
    public List<ProgramFileDto> getProgramFileList(Long programId);
}
