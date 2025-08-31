package com.connecple.connecple_backend.domain.program.repository;

import com.connecple.connecple_backend.domain.program.dto.ProgramDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.connecple.connecple_backend.domain.program.entity.QProgram.program;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProgramRepositoryImpl implements ProgramRepositoryQdsl {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<ProgramDto> getProgramList() {
        return queryFactory.select(Projections.constructor(ProgramDto.class, program))
                .from(program)
                .orderBy(program.sortOrder.asc())
                .fetch();
    }
}
