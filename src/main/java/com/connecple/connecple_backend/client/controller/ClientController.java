package com.connecple.connecple_backend.client.controller;

import com.connecple.connecple_backend.client.dto.HomeResponse;
import com.connecple.connecple_backend.client.service.ClientService;
import com.connecple.connecple_backend.domain.link.entity.dto.MainLinkResponseDto;
import com.connecple.connecple_backend.domain.link.service.MainLinkManagementService;
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

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> getHomeData() {
        HomeResponse homeData = clientService.getHomeData();
        return ResponseEntity.ok(homeData);
    }

    @GetMapping("links")
    public ResponseEntity<List<MainLinkResponseDto>> getMainLinks() {
        return ResponseEntity.ok(mainLinkManagementService.getMainLinks());
    }
}