package com.connecple.connecple_backend.domain.main.stats.service;

import com.connecple.connecple_backend.domain.main.stats.entity.MainStatsManagement;
import com.connecple.connecple_backend.domain.main.stats.entity.request.MainStatsUpdateRequest;
import com.connecple.connecple_backend.domain.main.stats.repository.MainStatsManagementRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainStatsManagementService {

    private final MainStatsManagementRepository mainStatsManagementRepository;

    @Transactional
    public void updateMainStats(List<MainStatsUpdateRequest> requestList) {

        // 1. sortOrder 중복 검사
        Set<Long> sortOrderSet = new HashSet<>();
        for (MainStatsUpdateRequest req : requestList) {
            if (!sortOrderSet.add(req.getSortOrder())) {
                throw new BaseException(400, "중복된 통계 항목이 입력되었습니다.");
            }
        }

        // 2. 기존 통계 삭제 (전체 초기화 후 재등록 방식)
        mainStatsManagementRepository.deleteAllInBatch();

        // 3. 새로 저장
        List<MainStatsManagement> mainStatsManagements = requestList.stream()
                .map(req -> MainStatsManagement.builder()
                        .statsName(req.getStatsName())
                        .statistic(req.getStatistic())
                        .unit(req.getUnit())
                        .sortOrder(req.getSortOrder())
                        .build())
                .toList();

        mainStatsManagementRepository.saveAll(mainStatsManagements);
    }
}
