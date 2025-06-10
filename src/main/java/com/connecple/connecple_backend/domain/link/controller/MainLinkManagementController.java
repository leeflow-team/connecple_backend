package com.connecple.connecple_backend.domain.link.controller;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

import com.connecple.connecple_backend.domain.link.entity.dto.MainLinkResponseDto;
import com.connecple.connecple_backend.domain.link.entity.response.MainLinkUpdateRequest;
import com.connecple.connecple_backend.domain.link.service.MainLinkManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/main-links")
public class MainLinkManagementController {

    private final MainLinkManagementService mainLinkManagementService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<MainLinkResponseDto>>> getMainLinks(HttpSession session) {
        checkAdmin(session);
        return ResponseEntity.ok(SuccessResponse.success(mainLinkManagementService.getMainLinks()));
    }

    @PatchMapping
    public ResponseEntity<SuccessResponse<Void>> updateMainLink(@RequestBody @Valid MainLinkUpdateRequest request) {
        mainLinkManagementService.updateMainLink(request);
        return ResponseEntity.ok(SuccessResponse.success());
    }
}
