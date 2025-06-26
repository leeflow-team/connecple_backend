package com.connecple.connecple_backend.domain.main.stats.repository;

import com.connecple.connecple_backend.domain.main.stats.entity.MainStatsManagement;
import java.util.List;

public interface MainStatsManagementRepositoryQdsl {
    List<MainStatsManagement> findAllOrderBySortOrder();
}
