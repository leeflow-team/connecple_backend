package com.connecple.connecple_backend.domain.main.stats.controller;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

import com.connecple.connecple_backend.domain.main.stats.entity.request.MainStatsUpdateRequest;
import com.connecple.connecple_backend.domain.main.stats.service.MainStatsManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/stats")
public class MainStatsManagementController {

    private final MainStatsManagementService mainStatsManagementService;

    @PostMapping
    public ResponseEntity<SuccessResponse> updateMainStats(HttpSession session, @RequestBody @Valid List<MainStatsUpdateRequest> request) {
        checkAdmin(session);
        mainStatsManagementService.updateMainStats(request);
        return ResponseEntity.ok(null);
    }
}
