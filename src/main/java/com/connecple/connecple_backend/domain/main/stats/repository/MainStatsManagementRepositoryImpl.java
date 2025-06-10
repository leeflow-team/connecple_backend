package com.connecple.connecple_backend.domain.main.stats.repository;

import com.connecple.connecple_backend.domain.main.stats.entity.MainStatsManagement;
import com.connecple.connecple_backend.domain.main.stats.entity.QMainStatsManagement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MainStatsManagementRepositoryImpl implements MainStatsManagementRepositoryQdsl {

    private final JPAQueryFactory queryFactory;

    QMainStatsManagement mainStatsManagement = QMainStatsManagement.mainStatsManagement;

    @Override
    public List<MainStatsManagement> findAllOrderBySortOrder() {
        return queryFactory.
                selectFrom(mainStatsManagement)
                .orderBy(mainStatsManagement.sortOrder.asc())
                .fetch();
    }
}
