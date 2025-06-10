package com.connecple.connecple_backend.domain.faq.repository;

import com.connecple.connecple_backend.domain.faq.entity.QFAQManagement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FAQManagementRepositoryImpl implements FAQManagementRepositoryQdsl {

    private final JPAQueryFactory queryFactory;
    QFAQManagement faq = QFAQManagement.fAQManagement;
}
