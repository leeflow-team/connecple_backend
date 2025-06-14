package com.connecple.connecple_backend.domain.faq.repository;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQManagementRepository extends JpaRepository<FAQManagement, Long>, FAQManagementRepositoryQdsl {
    Optional<FAQManagement> findByIdAndIsDeletedFalse(Long id);
    Page<FAQManagement> findAllByIsDeletedIsFalse(Pageable pageable);
    Page<FAQManagement> findAllByCategoryInAndIsDeletedFalse(List<String> categories, Pageable pageable);

}