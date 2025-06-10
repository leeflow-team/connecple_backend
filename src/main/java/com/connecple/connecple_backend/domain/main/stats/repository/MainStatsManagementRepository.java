package com.connecple.connecple_backend.domain.main.stats.repository;

import com.connecple.connecple_backend.domain.main.stats.entity.MainStatsManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainStatsManagementRepository extends JpaRepository<MainStatsManagement, Long>, MainStatsManagementRepositoryQdsl {
}
