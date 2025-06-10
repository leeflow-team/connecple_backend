package com.connecple.connecple_backend.domain.connecple.intro.service;

import com.connecple.connecple_backend.domain.connecple.intro.entity.ConnecpleIntroManagement;
import com.connecple.connecple_backend.domain.connecple.intro.entity.dto.ConnecpleHistoryResponse;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistoryBulkSaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistorySaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.repository.ConnecpleIntroManagementRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
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

    // 모든 히스토리 조회
    public List<ConnecpleHistoryResponse> getAllIntros() {
        List<ConnecpleIntroManagement> all = connecpleIntroManagementRepository.findAllOrderByYearDescAndSortOrder();

        // historyYear 기준으로 그룹핑 후 content를 줄바꿈으로 합침
        return all.stream()
                .collect(Collectors.groupingBy(
                        ConnecpleIntroManagement::getHistoryYear,
                        LinkedHashMap::new,
                        Collectors.mapping(ConnecpleIntroManagement::getContent, Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> new ConnecpleHistoryResponse(
                        entry.getKey(),
                        String.join("\n", entry.getValue())
                ))
                .collect(Collectors.toList());
    }

}
