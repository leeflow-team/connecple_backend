package com.connecple.connecple_backend.domain.story.repository;

import com.connecple.connecple_backend.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryQdsl {

}
