package com.connecple.connecple_backend.domain.main.intro.repository;

import com.connecple.connecple_backend.domain.main.intro.entity.MainIntroImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainIntroImageRepository extends JpaRepository<MainIntroImage, Long>, MainIntroImageRepositoryQdsl {
}
