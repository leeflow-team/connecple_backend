package com.connecple.connecple_backend.domain.program.repository;

import com.connecple.connecple_backend.domain.program.dto.ProgramDto;

import java.util.List;

public interface ProgramRepositoryQdsl {
    public List<ProgramDto> getProgramList();
}
