package com.connecple.connecple_backend.domain.connecple.intro.repository;

import com.connecple.connecple_backend.domain.connecple.intro.entity.ConnecpleIntroManagement;
import com.connecple.connecple_backend.domain.connecple.intro.entity.QConnecpleIntroManagement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConnecpleIntroManagementRepositoryImpl implements ConnecpleIntroManagementRepositoryQdsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ConnecpleIntroManagement> findAllOrderByYearDescAndSortOrder() {
        QConnecpleIntroManagement q = QConnecpleIntroManagement.connecpleIntroManagement;

        return queryFactory.selectFrom(q)
                .orderBy(q.historyYear.desc(), q.sortOrder.asc())
                .fetch();
    }
}