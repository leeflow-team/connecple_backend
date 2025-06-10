package com.connecple.connecple_backend.domain.connecple.intro.repository;

import com.connecple.connecple_backend.domain.connecple.intro.entity.ConnecpleIntroManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnecpleIntroManagementRepository extends JpaRepository<ConnecpleIntroManagement, Long> {
    void deleteByHistoryYear(String historyYear);
}
