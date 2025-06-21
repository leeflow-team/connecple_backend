package com.connecple.connecple_backend.domain.faq.service;

import com.connecple.connecple_backend.domain.faq.entity.FAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.FAQFile;
import com.connecple.connecple_backend.domain.faq.entity.QFAQManagement;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQAllResponse;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQDetailResponse;
import com.connecple.connecple_backend.domain.faq.entity.dto.FAQListResponse;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQCreateRequest;
import com.connecple.connecple_backend.domain.faq.entity.request.FAQUpdateRequest;
import com.connecple.connecple_backend.domain.faq.repository.FAQManagementRepository;
import com.connecple.connecple_backend.domain.faq.repository.FAQFileRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import com.connecple.connecple_backend.global.service.S3Service;
import com.querydsl.core.BooleanBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FAQManagementService {

    private final FAQManagementRepository faqManagementRepository;
    private final FAQFileRepository faqFileRepository;
    private final S3Service s3Service;

    @Transactional
    public void createFAQ(FAQCreateRequest request) {
        // FAQ 엔티티 생성 및 저장
        FAQManagement faq = FAQManagement.builder()
                .category(request.getCategory())
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .isActive(request.getIsActive())
                .isDeleted(false)
                .build();

        FAQManagement savedFAQ = faqManagementRepository.save(faq);

        // 파일 업로드 및 저장
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            uploadAndSaveFiles(request.getFiles(), savedFAQ);
        }
    }

    @Transactional
    public void createFAQ(String category, String question, String answer, Boolean isActive,
            List<MultipartFile> files) {
        // 입력 값 검증
        if (category == null || category.trim().isEmpty()) {
            throw new BaseException(400, "카테고리 설정은 필수입니다.");
        }
        if (question == null || question.trim().isEmpty()) {
            throw new BaseException(400, "질문 작성은 필수입니다.");
        }
        if (answer == null || answer.trim().isEmpty()) {
            throw new BaseException(400, "답변 작성은 필수입니다.");
        }
        if (isActive == null) {
            throw new BaseException(400, "상태 설정은 필수입니다.");
        }

        // FAQ 엔티티 생성 및 저장
        FAQManagement faq = FAQManagement.builder()
                .category(category.trim())
                .question(question.trim())
                .answer(answer.trim())
                .isActive(isActive)
                .isDeleted(false)
                .build();

        FAQManagement savedFAQ = faqManagementRepository.save(faq);

        // 파일 업로드 및 저장
        if (files != null && !files.isEmpty()) {
            uploadAndSaveFiles(files, savedFAQ);
        }
    }

    private void uploadAndSaveFiles(List<MultipartFile> files, FAQManagement faq) {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                // 파일 타입 검증
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.trim().isEmpty()) {
                    log.warn("파일명이 없는 파일이 업로드 시도됨");
                    continue;
                }

                // 파일 크기 검증 (100MB 제한)
                if (file.getSize() > 100 * 1024 * 1024) {
                    throw new BaseException(400, "파일 크기는 100MB를 초과할 수 없습니다: " + originalFilename);
                }

                // S3에 파일 업로드
                String filePath = s3Service.uploadFile(file, "faq-files");

                // 파일 확장자 추출
                String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

                // FAQFile 엔티티 생성 및 저장
                FAQFile faqFile = FAQFile.builder()
                        .originalFileName(originalFilename)
                        .storedFileName(extractFileNameFromPath(filePath))
                        .filePath(filePath)
                        .fileSize(file.getSize())
                        .fileType(fileType)
                        .faqManagement(faq)
                        .build();

                faqFileRepository.save(faqFile);

                log.info("파일 업로드 완료: {} -> {}", originalFilename, filePath);

            } catch (Exception e) {
                log.error("파일 업로드 실패: {}", file.getOriginalFilename(), e);
                throw new BaseException(500, "파일 업로드 중 오류가 발생했습니다: " + file.getOriginalFilename());
            }
        }
    }

    private String extractFileNameFromPath(String filePath) {
        if (filePath == null)
            return "";
        int lastSlash = filePath.lastIndexOf("/");
        return lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;
    }

    public FAQDetailResponse getFAQById(Long id) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(404, "해당 FAQ가 존재하지 않습니다."));

        // 파일들을 명시적으로 로딩 (LAZY 로딩 해결)
        List<FAQFile> files = faqFileRepository.findByFaqManagementId(id);
        faq.getFiles().clear();
        faq.getFiles().addAll(files);

        return FAQDetailResponse.fromEntity(faq);
    }

    @Transactional
    public void updateFAQ(Long id, FAQUpdateRequest request) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(404, "해당 FAQ가 존재하지 않습니다."));

        faq.update(request.getCategory(), request.getQuestion(), request.getAnswer(), request.getIsActive());
        faqManagementRepository.save(faq);
    }

    @Description("FAQ 삭제 - 이미 삭제된 항목은 또 삭제되지 않음")
    @Transactional
    public void deleteFAQ(Long id) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(400, "해당 FAQ를 찾을 수 없습니다."));

        // FAQ에 연결된 파일들을 S3에서 실제 삭제
        List<FAQFile> files = faqFileRepository.findByFaqManagementId(id);
        for (FAQFile file : files) {
            s3Service.deleteFile(file.getFilePath());
        }

        // FAQ soft delete
        faq.deleteFAQ();

        faqManagementRepository.save(faq);
    }

    @Transactional(readOnly = true)
    public FAQListResponse readAllFAQ(List<String> categories, int page, int size, String sortBy) {
        int pageSize = switch (size) {
            case 30 -> 30;
            case 50 -> 50;
            default -> 10;
        };

        Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<FAQManagement> pageResult;

        if (categories == null || categories.isEmpty()) {
            pageResult = faqManagementRepository.findAllByIsDeletedIsFalse(pageable);
        } else {
            pageResult = faqManagementRepository.findAllByCategoryInAndIsDeletedFalse(categories, pageable);
        }

        List<FAQAllResponse> resultList = pageResult.getContent().stream()
                .map(faq -> {
                    int fileCount = faqFileRepository.findByFaqManagementId(faq.getId()).size();
                    return new FAQAllResponse(
                            faq.getId(),
                            faq.getCategory(),
                            faq.getQuestion(),
                            faq.getIsActive(),
                            faq.getCreatedAt(),
                            fileCount);
                })
                .toList();

        return new FAQListResponse(
                resultList,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages());
    }

    @Transactional(readOnly = true)
    public FAQListResponse searchFAQ(String keyword, List<String> categories, int page, int size, String sortBy) {
        int pageSize = switch (size) {
            case 30 -> 30;
            case 50 -> 50;
            default -> 10;
        };

        Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        QFAQManagement faq = QFAQManagement.fAQManagement;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(faq.isDeleted.isFalse());

        // 카테고리 필터링
        if (categories != null && !categories.isEmpty()) {
            builder.and(faq.category.in(categories));
        }

        // 키워드는 question, answer에서만 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(
                    faq.question.containsIgnoreCase(keyword)
                            .or(faq.answer.containsIgnoreCase(keyword)));
        }

        Page<FAQManagement> faqPage = faqManagementRepository.findAllWithQueryDsl(builder, pageable, sortBy);

        List<FAQAllResponse> responseList = faqPage.getContent().stream()
                .map(f -> {
                    int fileCount = faqFileRepository.findByFaqManagementId(f.getId()).size();
                    return new FAQAllResponse(
                            f.getId(),
                            f.getCategory(),
                            f.getQuestion(),
                            f.getIsActive(),
                            f.getCreatedAt(),
                            fileCount);
                })
                .toList();

        return new FAQListResponse(
                responseList,
                faqPage.getTotalElements(),
                faqPage.getNumber(),
                faqPage.getSize(),
                faqPage.getTotalPages());
    }

}
