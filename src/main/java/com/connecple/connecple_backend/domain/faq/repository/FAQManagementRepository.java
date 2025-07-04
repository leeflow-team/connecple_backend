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

    // 클라이언트용: 활성화되고 삭제되지 않은 FAQ만 조회
    List<FAQManagement> findAllByIsActiveTrueAndIsDeletedFalseOrderByCreatedAtDesc();

    Optional<FAQManagement> findByIdAndIsActiveTrueAndIsDeletedFalse(Long id);

    // 클라이언트용: 페이징 지원
    Page<FAQManagement> findAllByIsActiveTrueAndIsDeletedFalse(Pageable pageable);

    Page<FAQManagement> findAllByCategoryInAndIsActiveTrueAndIsDeletedFalse(List<String> categories, Pageable pageable);

}