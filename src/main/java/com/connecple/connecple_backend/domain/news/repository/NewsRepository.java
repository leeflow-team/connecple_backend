package com.connecple.connecple_backend.domain.news.repository;

import com.connecple.connecple_backend.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryQdsl {

}
