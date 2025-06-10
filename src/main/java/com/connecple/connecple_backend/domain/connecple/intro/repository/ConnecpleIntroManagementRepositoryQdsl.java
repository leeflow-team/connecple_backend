package com.connecple.connecple_backend.domain.connecple.intro.repository;

import com.connecple.connecple_backend.domain.connecple.intro.entity.ConnecpleIntroManagement;
import java.util.List;

public interface ConnecpleIntroManagementRepositoryQdsl {
    List<ConnecpleIntroManagement> findAllOrderByYearDescAndSortOrder();
}