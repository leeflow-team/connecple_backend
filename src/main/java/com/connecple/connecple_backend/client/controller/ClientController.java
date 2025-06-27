package com.connecple.connecple_backend.client.controller;

import com.connecple.connecple_backend.client.dto.HomeResponse;
import com.connecple.connecple_backend.client.service.ClientService;
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

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> getHomeData() {
        HomeResponse homeData = clientService.getHomeData();
        return ResponseEntity.ok(homeData);
    }
}