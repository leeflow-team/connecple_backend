package com.connecple.connecple_backend.domain.link.repository;

import com.connecple.connecple_backend.domain.link.entity.MainLinkManagement;
import com.connecple.connecple_backend.domain.main.link.entity.QMainLinkManagement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MainLinkManagementRepositoryImpl implements MainLinkManagementRepositoryQdsl {

    private final JPAQueryFactory queryFactory;
    QMainLinkManagement link = QMainLinkManagement.mainLinkManagement;

    @Override
    public List<MainLinkManagement> findAllLinks() {
        return queryFactory.selectFrom(link).fetch();
    }
}
