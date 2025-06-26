package com.connecple.connecple_backend.domain.link.service;

import com.connecple.connecple_backend.domain.link.entity.MainLinkManagement;
import com.connecple.connecple_backend.domain.link.entity.dto.MainLinkResponseDto;
import com.connecple.connecple_backend.domain.link.entity.response.MainLinkUpdateRequest;
import com.connecple.connecple_backend.domain.link.repository.MainLinkManagementRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MainLinkManagementService {

    private final MainLinkManagementRepository mainLinkManagementRepository;

    public List<MainLinkResponseDto> getMainLinks() {
        return mainLinkManagementRepository.findAllLinks().stream()
                .map(MainLinkResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateMainLink(MainLinkUpdateRequest request) {
        MainLinkManagement link = mainLinkManagementRepository.findByTitle(request.getTitle())
                .orElseThrow(() -> new BaseException(400, "해당 타이틀의 링크가 존재하지 않습니다."));

        link.updateLinkPath(request.getLinkPath());
        mainLinkManagementRepository.save(link);
    }
}
