package com.connecple.connecple_backend.domain.faq.repository;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQManagementRepository extends JpaRepository<FAQManagement, Long>, FAQManagementRepositoryQdsl {
}