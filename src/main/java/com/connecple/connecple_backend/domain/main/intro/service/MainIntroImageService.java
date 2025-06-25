package com.connecple.connecple_backend.domain.main.intro.service;

import com.connecple.connecple_backend.domain.main.intro.entity.MainIntroImage;
import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.intro.repository.MainIntroImageRepository;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageCreateRequest;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageUpdateRequest;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageBulkSaveRequest;
import com.connecple.connecple_backend.global.exception.BaseException;
import com.connecple.connecple_backend.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class MainIntroImageService {
    private final MainIntroImageRepository mainIntroImageRepository;
    private final S3Service s3Service;

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

    public List<MainIntroImageDto> getMainIntroImageList() {
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
            MainIntroImage image = mainIntroImageRepository.findById(id)
                    .orElseThrow(() -> new BaseException(404, "The main intro image does not exist"));

            // S3에서 파일 삭제
            s3Service.deleteFile(image.getImagePath());

            // DB에서 삭제
            mainIntroImageRepository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(404, "The main intro image does not exist");
        }
    }

    @Transactional
    public void resetMainIntroImages(MainIntroImageBulkSaveRequest request) {
        // 입력 데이터 검증
        List<MultipartFile> images = request.getImages();
        List<String> titles = request.getTitles();
        List<String> companies = request.getCompanies();

        if (images == null || titles == null || companies == null) {
            throw new BaseException(400, "이미지, 제목, 회사 목록은 필수입니다");
        }

        if (images.size() != titles.size() || images.size() != companies.size()) {
            throw new BaseException(400, "이미지, 제목, 회사 목록의 크기가 일치해야 합니다");
        }

        if (images.size() > 10) {
            throw new BaseException(400, "최대 10개의 이미지만 업로드할 수 있습니다");
        }

        // 기존 데이터의 S3 파일들 삭제
        List<MainIntroImage> existingImages = mainIntroImageRepository.findAll();
        for (MainIntroImage existingImage : existingImages) {
            s3Service.deleteFile(existingImage.getImagePath());
        }

        // 기존 데이터 모두 삭제
        mainIntroImageRepository.deleteAll();

        // 새로운 데이터 순서대로 저장
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String title = titles.get(i);
            String company = companies.get(i);

            // S3에 업로드
            String imagePath = s3Service.uploadFile(image, "main-intro-images");

            MainIntroImage newImage = MainIntroImage.builder()
                    .imagePath(imagePath)
                    .sortOrder((long) (i + 1)) // 1부터 시작하는 순서
                    .title(title)
                    .company(company)
                    .build();

            mainIntroImageRepository.save(newImage);
        }
    }
}
