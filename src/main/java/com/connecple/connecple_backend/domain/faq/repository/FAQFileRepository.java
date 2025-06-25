package com.connecple.connecple_backend.domain.faq.repository;

import com.connecple.connecple_backend.domain.faq.entity.FAQFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQFileRepository extends JpaRepository<FAQFile, Long> {
    List<FAQFile> findByFaqManagementId(Long faqId);

    void deleteByFaqManagementId(Long faqId);
}