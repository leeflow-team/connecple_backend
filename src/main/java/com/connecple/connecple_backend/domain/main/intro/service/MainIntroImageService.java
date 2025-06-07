package com.connecple.connecple_backend.domain.main.intro.service;

import com.connecple.connecple_backend.domain.main.intro.entity.MainIntroImage;
import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.intro.repository.MainIntroImageRepository;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageCreateRequest;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageUpdateRequest;
import com.connecple.connecple_backend.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class MainIntroImageService {
    private final MainIntroImageRepository mainIntroImageRepository;

    @Transactional
    public Long createMainIntroImage(MainIntroImageCreateRequest request) {
        MainIntroImage data = MainIntroImage.builder()
                .imagePath(request.getImagePath())
                .sortOrder(request.getSortOrder())
                .title(request.getTitle())
                .company(request.getCompany())
                .build();

        MainIntroImage savedData = mainIntroImageRepository.save(data);
        return savedData.getId();
    }

    public List<MainIntroImageDto> getMainIntroImageList(){
        return mainIntroImageRepository.getMainIntroImages();
    }

    @Transactional
    public MainIntroImageDto updateMainIntroImage(MainIntroImageUpdateRequest request, Long id) {
        MainIntroImage data = mainIntroImageRepository.findById(id)
                .orElseThrow(() -> new BaseException(404, "Main intro image not found"));

        return data.updateEntity(request);
    }

    @Transactional
    public void deleteMainIntroImage(Long id) {
        try {
            mainIntroImageRepository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(404, "The main intro image does not exist");
        }

    }
}
