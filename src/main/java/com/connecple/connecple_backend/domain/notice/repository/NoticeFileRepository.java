package com.connecple.connecple_backend.domain.notice.repository;

import com.connecple.connecple_backend.domain.notice.entity.NoticeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {

    List<NoticeFile> findByNoticeManagementId(Long noticeManagementId);

    @Modifying
    @Query("DELETE FROM NoticeFile nf WHERE nf.noticeManagement.id = :noticeManagementId")
    void deleteByNoticeManagementId(@Param("noticeManagementId") Long noticeManagementId);
}