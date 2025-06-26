package com.connecple.connecple_backend.domain.faq.repository;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FAQManagementRepositoryQdsl {
    Page<FAQManagement> findAllWithQueryDsl(BooleanBuilder builder, Pageable pageable, String sortBy);

}