package com.connecple.connecple_backend.domain.link.repository;

import com.connecple.connecple_backend.domain.link.entity.MainLinkManagement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainLinkManagementRepository extends JpaRepository<MainLinkManagement, Long>, MainLinkManagementRepositoryQdsl {
    Optional<MainLinkManagement> findByTitle(String title);
}
