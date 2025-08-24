package com.connecple.connecple_backend.domain.news.service;

import com.connecple.connecple_backend.domain.news.dto.NewsDto;
import com.connecple.connecple_backend.domain.news.entity.News;
import com.connecple.connecple_backend.domain.news.entity.request.NewsBulkSaveRequest;
import com.connecple.connecple_backend.domain.news.repository.NewsRepository;
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
public class NewsService {
    private final NewsRepository newsRepository;
    private final S3Service s3Service;

    public List<NewsDto> getNewsList() {
        return newsRepository.getNews();
    }

    @Transactional

    public void resetNewsInfo(NewsBulkSaveRequest request) {
        // 입력 데이터 검증
        List<MultipartFile> images = request.getImages();
        List<String> titles = request.getTitles();
        List<String> centents = request.getContents();
        List<String> newsUrlList = request.getNewsUrlList();

        if (images == null || titles == null || centents == null) {
            throw new BaseException(400, "이미지, 제목, 내용 목록은 필수입니다");
        }

        if (images.size() != titles.size() || images.size() != centents.size()) {
            throw new BaseException(400, "이미지, 제목, 회사 목록의 크기가 일치해야 합니다");
        }

        if (images.size() > 3) {
            throw new BaseException(400, "최대 3개의 이미지만 업로드할 수 있습니다");
        }

        // 모든 이미지 파일 검증
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            validateImageFile(image, i + 1);
        }

        // 기존 데이터의 S3 파일들 삭제
        List<News> existingImages = newsRepository.findAll();
        for (News existingImage : existingImages) {
            s3Service.deleteFile(existingImage.getImagePath());
        }

        // 기존 데이터 모두 삭제
        newsRepository.deleteAll();

        // 새로운 데이터 순서대로 저장
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String title = titles.get(i);
            String content = centents.get(i);
            String newsUrl = newsUrlList.get(i);

            // S3에 업로드
            String imagePath = s3Service.uploadFile(image, "news");

            News news = News.builder()
                    .imagePath(imagePath)
                    .sortOrder((long) (i + 1))
                    .title(title)
                    .content(content)
                    .newsUrl(newsUrl)
                    .build();

            newsRepository.save(news);
        }
    }

    private void validateImageFile(MultipartFile imageFile, int index) {
        // 파일이 비어있는지 검증
        if (imageFile == null || imageFile.isEmpty()) {
            throw new BaseException(400, index + "번째 이미지 파일이 비어있습니다.");
        }

        // 파일명 검증
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BaseException(400, index + "번째 이미지 파일명이 없습니다.");
        }

        // 파일 크기 검증 (10MB 제한)
        if (imageFile.getSize() > 10 * 1024 * 1024) {
            throw new BaseException(413, index + "번째 이미지 파일 크기는 10MB를 초과할 수 없습니다.");
        }

        // 파일 확장자 검증 (jpg, jpeg, png만 허용)
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png")) {
            throw new BaseException(415, index + "번째 파일은 이미지 파일만 업로드 가능합니다. (jpg, jpeg, png만 허용)");
        }

        // MIME 타입 검증 (추가 보안)
        String contentType = imageFile.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/jpg")
                        && !contentType.equals("image/png"))) {
            throw new BaseException(400, index + "번째 파일은 이미지 파일만 업로드 가능합니다. (jpg, jpeg, png만 허용)");
        }

        log.info("{}번째 이미지 파일 검증 완료: {} (크기: {} bytes, 타입: {})",
                index, originalFilename, imageFile.getSize(), contentType);
    }
}
