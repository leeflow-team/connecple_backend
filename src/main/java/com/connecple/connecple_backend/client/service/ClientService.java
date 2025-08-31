package com.connecple.connecple_backend.client.service;

import com.connecple.connecple_backend.client.dto.ClientProgramResponse;
import com.connecple.connecple_backend.client.dto.HomeResponse;
import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.intro.service.MainIntroImageService;
import com.connecple.connecple_backend.domain.main.stats.entity.dto.MainStatsResponseDto;
import com.connecple.connecple_backend.domain.main.stats.service.MainStatsManagementService;
import com.connecple.connecple_backend.domain.program.dto.ProgramDto;
import com.connecple.connecple_backend.domain.program.dto.ProgramFileDto;
import com.connecple.connecple_backend.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class ClientService {

    private final MainIntroImageService mainIntroImageService;
    private final MainStatsManagementService mainStatsManagementService;
    private final ProgramService programService;

    public HomeResponse getHomeData() {
        List<MainIntroImageDto> introImages = mainIntroImageService.getMainIntroImageList();
        List<MainStatsResponseDto> stats = mainStatsManagementService.getMainStats();

        return new HomeResponse(introImages, stats);
    }

    public List<ClientProgramResponse> getProgramData() {
        List<ClientProgramResponse> result = new ArrayList<>();
        List<ProgramDto> programList = programService.getProgramList();
        for (ProgramDto program : programList) {
            List<ProgramFileDto> programFileList = programService.getProgramFileList(program.getId());
            if (programFileList.isEmpty()) {
                ProgramFileDto dto = new ProgramFileDto(program.getId(), program.getId(), program.getImagePath());
                programFileList.add(dto);
            }
            ClientProgramResponse response = new ClientProgramResponse(program, programFileList);
            result.add(response);
        }
        return result;
    }
}