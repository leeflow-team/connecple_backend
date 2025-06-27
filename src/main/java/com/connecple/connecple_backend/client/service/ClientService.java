package com.connecple.connecple_backend.client.service;

import com.connecple.connecple_backend.client.dto.HomeResponse;
import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.intro.service.MainIntroImageService;
import com.connecple.connecple_backend.domain.main.stats.entity.dto.MainStatsResponseDto;
import com.connecple.connecple_backend.domain.main.stats.service.MainStatsManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class ClientService {

    private final MainIntroImageService mainIntroImageService;
    private final MainStatsManagementService mainStatsManagementService;

    public HomeResponse getHomeData() {
        List<MainIntroImageDto> introImages = mainIntroImageService.getMainIntroImageList();
        List<MainStatsResponseDto> stats = mainStatsManagementService.getMainStats();

        return new HomeResponse(introImages, stats);
    }
}