package com.connecple.connecple_backend.domain.faq.service;

import com.connecple.connecple_backend.client.dto.ClientFAQDetailResponse;
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

    // 클라이언트용: 활성화된 FAQ만 조회
    public ClientFAQDetailResponse getClientFAQById(Long id) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(404, "해당 FAQ가 존재하지 않습니다."));

        // 파일들을 명시적으로 로딩 (LAZY 로딩 해결)
        List<FAQFile> files = faqFileRepository.findByFaqManagementId(id);
        faq.getFiles().clear();
        faq.getFiles().addAll(files);

        return ClientFAQDetailResponse.fromEntity(faq);
    }

    @Transactional
    public void updateFAQ(Long id, String category, String question, String answer, Boolean isActive,
            List<Long> deleteFileIds, List<MultipartFile> files) {

        // FAQ 엔티티 조회 및 업데이트
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(404, "해당 FAQ가 존재하지 않습니다."));

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

        faq.update(category.trim(), question.trim(), answer.trim(), isActive);
        faqManagementRepository.save(faq);

        // 선택적 파일 삭제 및 새 파일 추가
        updateFilesSelectively(id, deleteFileIds, files, faq);
    }

    private void updateFilesSelectively(Long faqId, List<Long> deleteFileIds, List<MultipartFile> newFiles,
            FAQManagement faq) {
        log.info("선택적 파일 업데이트 시작 - FAQ ID: {}, 삭제할 파일 ID: {}, 새 파일 개수: {}",
                faqId, deleteFileIds, newFiles != null ? newFiles.size() : 0);

        // 1. 삭제할 파일들 처리
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            for (Long fileId : deleteFileIds) {
                // 해당 파일이 이 FAQ의 파일인지 검증
                FAQFile fileToDelete = faqFileRepository.findById(fileId)
                        .orElseThrow(() -> new BaseException(404, "삭제하려는 파일을 찾을 수 없습니다. 파일 ID: " + fileId));

                if (!fileToDelete.getFaqManagement().getId().equals(faqId)) {
                    throw new BaseException(400, "해당 FAQ의 파일이 아닙니다. 파일 ID: " + fileId);
                }

                try {
                    // S3에서 파일 삭제
                    s3Service.deleteFile(fileToDelete.getFilePath());
                    log.info("S3에서 파일 삭제 완료: {}", fileToDelete.getFilePath());
                } catch (Exception e) {
                    log.error("S3 파일 삭제 실패: {} - 오류: {}", fileToDelete.getFilePath(), e.getMessage());
                    // S3 삭제 실패해도 DB에서는 제거 (데이터 일관성 유지)
                }

                // DB에서 파일 레코드 삭제
                faqFileRepository.delete(fileToDelete);
                log.info("DB에서 파일 레코드 삭제 완료 - 파일 ID: {}", fileId);
            }
        }

        // 2. 새로운 파일들 업로드 및 저장
        if (newFiles != null && !newFiles.isEmpty()) {
            uploadAndSaveFiles(newFiles, faq);
            log.info("새로운 파일들 업로드 완료 - 개수: {}", newFiles.size());
        }

        log.info("선택적 파일 업데이트 완료");
    }

    @Description("FAQ 삭제 - FAQ는 soft delete, 파일은 실제 삭제")
    @Transactional
    public void deleteFAQ(Long id) {
        FAQManagement faq = faqManagementRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BaseException(400, "해당 FAQ를 찾을 수 없습니다."));

        // FAQ에 연결된 파일들을 실제 삭제 (S3 + DB)
        List<FAQFile> files = faqFileRepository.findByFaqManagementId(id);

        log.info("FAQ {} 삭제 시작 - 연결된 파일 수: {}", id, files.size());

        // S3에서 파일들 실제 삭제
        int deletedFileCount = 0;
        int failedFileCount = 0;

        for (FAQFile file : files) {
            try {
                s3Service.deleteFile(file.getFilePath());
                deletedFileCount++;
                log.info("S3 파일 삭제 성공: {}", file.getFilePath());
            } catch (Exception e) {
                failedFileCount++;
                log.error("S3 파일 삭제 실패: {} - 오류: {}", file.getFilePath(), e.getMessage());
                // S3 삭제 실패해도 계속 진행 (DB 정리를 위해)
            }
        }

        // DB에서 FAQFile 레코드들 모두 삭제
        if (!files.isEmpty()) {
            faqFileRepository.deleteByFaqManagementId(id);
            log.info("DB에서 FAQFile 레코드들 삭제 완료");
        }

        // FAQ soft delete
        faq.deleteFAQ();
        faqManagementRepository.save(faq);

        log.info("FAQ {} 삭제 완료 - S3 파일 삭제 성공: {}, 실패: {}", id, deletedFileCount, failedFileCount);
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
