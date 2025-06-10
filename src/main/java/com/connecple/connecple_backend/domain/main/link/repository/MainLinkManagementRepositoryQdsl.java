package com.connecple.connecple_backend.domain.main.link.repository;

import com.connecple.connecple_backend.domain.main.link.entity.MainLinkManagement;
import java.util.List;

public interface MainLinkManagementRepositoryQdsl {
    List<MainLinkManagement> findAllLinks();
}

