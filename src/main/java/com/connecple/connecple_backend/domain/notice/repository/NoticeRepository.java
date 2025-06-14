package com.connecple.connecple_backend.domain.notice.repository;

import com.connecple.connecple_backend.domain.notice.entity.NoticeManagement;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeManagement, Long> {
  Optional<NoticeManagement> findByIdAndIsDeletedIsFalse(Long id);
  Page<NoticeManagement> findAllByIsDeletedIsFalse(Pageable pageable);
  Page<NoticeManagement> findAllByIsDeletedIsFalseAndCategory(String category, Pageable pageable); // ✅ 추가

}
