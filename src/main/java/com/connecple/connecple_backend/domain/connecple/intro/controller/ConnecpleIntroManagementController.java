package com.connecple.connecple_backend.domain.connecple.intro.controller;

import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistoryBulkSaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.entity.request.ConnecpleHistorySaveRequest;
import com.connecple.connecple_backend.domain.connecple.intro.service.ConnecpleIntroManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}