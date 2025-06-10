package com.connecple.connecple_backend.domain.main.link.service;

import com.connecple.connecple_backend.domain.main.link.dto.MainLinkResponseDto;
import com.connecple.connecple_backend.domain.main.link.repository.MainLinkManagementRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainLinkManagementService {

    private final MainLinkManagementRepository mainLinkManagementRepository;

    public List<MainLinkResponseDto> getMainLinks() {
        return mainLinkManagementRepository.findAllLinks().stream()
                .map(MainLinkResponseDto::fromEntity)
                .toList();
    }
}
