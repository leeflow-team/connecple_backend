package com.connecple.connecple_backend.domain.link.repository;

import com.connecple.connecple_backend.domain.link.entity.MainLinkManagement;
import java.util.List;

public interface MainLinkManagementRepositoryQdsl {
    List<MainLinkManagement> findAllLinks();
}

