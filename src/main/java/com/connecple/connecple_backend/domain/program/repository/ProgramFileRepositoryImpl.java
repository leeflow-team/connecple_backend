package com.connecple.connecple_backend.domain.program.repository;

import com.connecple.connecple_backend.domain.program.dto.ProgramFileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.connecple.connecple_backend.domain.program.entity.QProgramFile.programFile;

@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class ProgramFileRepositoryImpl implements ProgramFileRepositoryQdsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProgramFileDto> getProgramFileList(Long programId) {
        return queryFactory.select(Projections.constructor(ProgramFileDto.class, programFile))
                .from(programFile)
                .where(programFile.programId.eq(programId))
                .orderBy(programFile.sortOrder.asc())
                .fetch();
    }
}
