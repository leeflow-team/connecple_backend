package com.connecple.connecple_backend.domain.program.repository;

import com.connecple.connecple_backend.domain.program.entity.ProgramFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramFileRepository extends JpaRepository<ProgramFile, Long>, ProgramFileRepositoryQdsl {
    public void deleteAllByProgramId(Long programId);
}
