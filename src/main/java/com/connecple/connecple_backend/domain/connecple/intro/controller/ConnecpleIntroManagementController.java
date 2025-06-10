package com.connecple.connecple_backend.domain.connecple.intro.controller;

import com.connecple.connecple_backend.domain.connecple.intro.entity.dto.ConnecpleHistoryResponse;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistoryBulkSaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistorySaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.service.ConnecpleIntroManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/intro")
public class ConnecpleIntroManagementController {

    private final ConnecpleIntroManagementService connecpleIntroManagementService;

    @PostMapping("/history")
    public ResponseEntity<SuccessResponse<Void>> saveAllHistories(@RequestBody @Valid ConnecpleHistoryBulkSaveRequest request) {
        connecpleIntroManagementService.saveAllHistories(request);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    /**
     * 모든 회사 연혁 조회 (historyYear 기준 내림차순, content는 줄바꿈으로 연결)
     */
    @GetMapping("/history")
    public ResponseEntity<SuccessResponse<List<ConnecpleHistoryResponse>>> getAllIntroHistories() {
        List<ConnecpleHistoryResponse> response = connecpleIntroManagementService.getAllIntros();
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}