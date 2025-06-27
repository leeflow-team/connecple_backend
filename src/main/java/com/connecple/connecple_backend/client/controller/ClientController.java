package com.connecple.connecple_backend.client.controller;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

import com.connecple.connecple_backend.client.dto.HomeResponse;
import com.connecple.connecple_backend.client.service.ClientService;
import com.connecple.connecple_backend.domain.connecple.intro.entity.dto.ConnecpleHistoryResponse;
import com.connecple.connecple_backend.domain.connecple.intro.service.ConnecpleIntroManagementService;
import com.connecple.connecple_backend.domain.link.entity.dto.MainLinkResponseDto;
import com.connecple.connecple_backend.domain.link.service.MainLinkManagementService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ClientController {

    private final ClientService clientService;
    private final MainLinkManagementService mainLinkManagementService;
    private final ConnecpleIntroManagementService connecpleIntroManagementService;

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> getHomeData() {
        HomeResponse homeData = clientService.getHomeData();
        return ResponseEntity.ok(homeData);
    }

    @GetMapping("/links")
    public ResponseEntity<List<MainLinkResponseDto>> getMainLinks() {
        return ResponseEntity.ok(mainLinkManagementService.getMainLinks());
    }

    /**
     * 모든 회사 연혁 조회 (historyYear 기준 내림차순, content는 줄바꿈으로 연결)
     */
    @GetMapping("/history")
    public ResponseEntity<List<ConnecpleHistoryResponse>> getAllIntroHistories() {
        List<ConnecpleHistoryResponse> response = connecpleIntroManagementService.getAllIntros();
        return ResponseEntity.ok(response);
    }
}