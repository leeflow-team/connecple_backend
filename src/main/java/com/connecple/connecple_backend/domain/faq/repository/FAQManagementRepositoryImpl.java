package com.connecple.connecple_backend.domain.faq.repository;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.QFAQManagement;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FAQManagementRepositoryImpl implements FAQManagementRepositoryQdsl {

    private final JPAQueryFactory queryFactory;
    QFAQManagement faq = QFAQManagement.fAQManagement;

    @Override
    public Page<FAQManagement> findAllWithQueryDsl(BooleanBuilder builder, Pageable pageable, String sortBy) {
        List<FAQManagement> faqList = queryFactory
                .selectFrom(faq)
                .where(builder)
                .orderBy("createdAt".equals(sortBy) ? faq.createdAt.desc() : faq.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(faq)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(faqList, pageable, total);
    }
}
