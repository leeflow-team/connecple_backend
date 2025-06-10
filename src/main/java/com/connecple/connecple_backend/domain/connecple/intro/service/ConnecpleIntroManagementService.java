package com.connecple.connecple_backend.domain.connecple.intro.service;

import com.connecple.connecple_backend.domain.connecple.intro.entity.ConnecpleIntroManagement;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistoryBulkSaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistorySaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.repository.ConnecpleIntroManagementRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnecpleIntroManagementService {

    private final ConnecpleIntroManagementRepository connecpleIntroManagementRepository;

    @Transactional
    public void saveAllHistories(ConnecpleHistoryBulkSaveRequest request) {
        // 전체 삭제
        connecpleIntroManagementRepository.deleteAllInBatch();

        if (request.getHistoryList().isEmpty()) {
            return; // 저장할 항목 없으면 종료
        }

        List<ConnecpleIntroManagement> entities = new ArrayList<>();

        for (ConnecpleHistorySaveRequest history : request.getHistoryList()) {
            String[] lines = history.getContent().split("\\r?\\n");

            for (int i = 0; i < lines.length; i++) {
                String content = lines[i].trim();
                if (!content.isEmpty()) {
                    entities.add(ConnecpleIntroManagement.builder()
                            .historyYear(history.getHistoryYear())
                            .content(content)
                            .sortOrder((long) (i + 1))
                            .build());
                }
            }
        }

        connecpleIntroManagementRepository.saveAll(entities);
    }
}
