package com.connecple.connecple_backend.domain.program.repository;

import com.connecple.connecple_backend.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long>, ProgramRepositoryQdsl {
}
