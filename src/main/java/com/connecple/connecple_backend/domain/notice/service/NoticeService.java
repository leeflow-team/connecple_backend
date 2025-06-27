package com.connecple.connecple_backend.domain.notice.service;

import com.connecple.connecple_backend.client.dto.ClientNoticeAllResponse;
import com.connecple.connecple_backend.client.dto.ClientNoticeDetailResponse;
import com.connecple.connecple_backend.client.dto.ClientNoticeListResponse;
import com.connecple.connecple_backend.domain.notice.dto.req.NoticeCreateRequest;
import com.connecple.connecple_backend.domain.notice.dto.req.NoticeUpdateRequest;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeAllResponse;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeDetailResponse;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeListResponse;
import com.connecple.connecple_backend.domain.notice.entity.NoticeFile;
import com.connecple.connecple_backend.domain.notice.entity.NoticeManagement;
import com.connecple.connecple_backend.domain.notice.entity.QNoticeManagement;
import com.connecple.connecple_backend.domain.notice.repository.NoticeFileRepository;
import com.connecple.connecple_backend.domain.notice.repository.NoticeRepository;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import com.connecple.connecple_backend.global.exception.BaseException;
import com.connecple.connecple_backend.global.service.S3Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final NoticeFileRepository noticeFileRepository;
  private final S3Service s3Service;
  private final JPAQueryFactory jpaQueryFactory;

  @Description("공지 생성")
  @Transactional
  public void createNotice(NoticeCreateRequest request) {
    // Notice 엔티티 생성 및 저장
    NoticeManagement noticeManagement = new NoticeManagement(request.getCategory(), request.getTitle(),
        request.getContent(), request.getIsActive());

    NoticeManagement savedNotice = noticeRepository.save(noticeManagement);

    // 파일 업로드 및 저장
    if (request.getFiles() != null && !request.getFiles().isEmpty()) {
      uploadAndSaveFiles(request.getFiles(), savedNotice);
    }
  }

  @Transactional
  public void createNotice(String category, String title, String content, Boolean isActive,
      List<MultipartFile> files) {
    // 디버깅을 위한 로그 추가
    log.info("createNotice 호출됨 - category: {}, title: {}, files: {}", category, title, files);
    if (files != null) {
      log.info("files 크기: {}", files.size());
      for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        log.info("파일 {}: 이름={}, 크기={}, 비어있음={}", i, file.getOriginalFilename(), file.getSize(), file.isEmpty());
      }
    } else {
      log.info("files가 null입니다");
    }

    // 입력 값 검증
    if (category == null || category.trim().isEmpty()) {
      throw new BaseException(400, "카테고리 설정은 필수입니다.");
    }
    if (title == null || title.trim().isEmpty()) {
      throw new BaseException(400, "제목 작성은 필수입니다.");
    }
    if (content == null || content.trim().isEmpty()) {
      throw new BaseException(400, "내용 작성은 필수입니다.");
    }
    if (isActive == null) {
      throw new BaseException(400, "상태 설정은 필수입니다.");
    }

    // Notice 엔티티 생성 및 저장
    NoticeManagement notice = NoticeManagement.builder()
        .category(category.trim())
        .title(title.trim())
        .content(content.trim())
        .isActive(isActive)
        .isDeleted(false)
        .build();

    NoticeManagement savedNotice = noticeRepository.save(notice);
    log.info("Notice 저장 완료 - ID: {}", savedNotice.getId());

    // 파일 업로드 및 저장
    if (files != null && !files.isEmpty()) {
      log.info("파일 업로드 시작 - 파일 개수: {}", files.size());
      uploadAndSaveFiles(files, savedNotice);
    } else {
      log.info("업로드할 파일이 없습니다");
    }
  }

  private void uploadAndSaveFiles(List<MultipartFile> files, NoticeManagement notice) {
    log.info("uploadAndSaveFiles 시작 - Notice ID: {}, 파일 개수: {}", notice.getId(), files.size());

    for (MultipartFile file : files) {
      if (file.isEmpty()) {
        log.warn("빈 파일 건너뜀: {}", file.getOriginalFilename());
        continue;
      }

      try {
        // 파일 타입 검증
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
          log.warn("파일명이 없는 파일이 업로드 시도됨");
          continue;
        }

        log.info("파일 처리 시작: {}", originalFilename);

        // 파일 크기 검증 (100MB 제한)
        if (file.getSize() > 100 * 1024 * 1024) {
          throw new BaseException(400, "파일 크기는 100MB를 초과할 수 없습니다: " + originalFilename);
        }

        // S3에 파일 업로드
        log.info("S3 업로드 시작: {}", originalFilename);
        String filePath = s3Service.uploadFile(file, "notice-files");
        log.info("S3 업로드 완료: {} -> {}", originalFilename, filePath);

        // 파일 확장자 추출
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        // NoticeFile 엔티티 생성 및 저장
        NoticeFile noticeFile = NoticeFile.builder()
            .originalFileName(originalFilename)
            .storedFileName(extractFileNameFromPath(filePath))
            .filePath(filePath)
            .fileSize(file.getSize())
            .fileType(fileType)
            .noticeManagement(notice)
            .build();

        log.info("NoticeFile 엔티티 생성 완료: {}", noticeFile.getOriginalFileName());

        NoticeFile savedFile = noticeFileRepository.save(noticeFile);
        log.info("NoticeFile DB 저장 완료 - ID: {}", savedFile.getId());

        log.info("파일 업로드 완료: {} -> {}", originalFilename, filePath);

      } catch (Exception e) {
        log.error("파일 업로드 실패: {}", file.getOriginalFilename(), e);
        throw new BaseException(500, "파일 업로드 중 오류가 발생했습니다: " + file.getOriginalFilename());
      }
    }

    log.info("uploadAndSaveFiles 완료");
  }

  private String extractFileNameFromPath(String filePath) {
    if (filePath == null)
      return "";
    int lastSlash = filePath.lastIndexOf("/");
    return lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;
  }

  @Description("공지 전체 조회 (페이지네이션, 10, 30, 50 개씩 보기 가능, 삭제된 것은 조회 안됨, 전체 개수도 카운트해서 반환)")
  @Transactional(readOnly = true)
  public NoticeListResponse readAllNotice(List<String> categories, int page, int size, String sortBy) {
    int pageSize = switch (size) {
      case 30 -> 30;
      case 50 -> 50;
      default -> 10;
    };

    Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
    Pageable pageable = PageRequest.of(page, pageSize, sort);
    Page<NoticeManagement> pageResult;
    if (categories == null || categories.isEmpty()) {
      pageResult = noticeRepository.findAllByIsDeletedIsFalse(pageable);
    } else {
      pageResult = noticeRepository.findAllByCategoryInAndIsDeletedFalse(categories, pageable);
    }

    List<NoticeAllResponse> responseList = pageResult.getContent().stream()
        .map(notice -> {
          int fileCount = noticeFileRepository.findByNoticeManagementId(notice.getId()).size();
          return new NoticeAllResponse(
              notice.getId(),
              notice.getCategory(),
              notice.getTitle(),
              notice.getIsActive(),
              notice.getCreatedAt(),
              fileCount);
        })
        .toList();

    return new NoticeListResponse(
        responseList,
        pageResult.getTotalElements(),
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalPages());
  }

  @Description("공지 상세 조회 (삭제된 것은 조회 안됨)")
  @Transactional(readOnly = true)
  public NoticeDetailResponse readDetailNotice(@PathVariable("id") Long id) {
    NoticeManagement noticeManagement = noticeRepository.findByIdAndIsDeletedIsFalse(id)
        .orElseThrow(() -> new BaseException(400, "해당 공지사항을 찾을 수 없습니다"));

    // 파일들을 명시적으로 로딩 (LAZY 로딩 해결)
    List<NoticeFile> files = noticeFileRepository.findByNoticeManagementId(id);
    noticeManagement.getFiles().clear();
    noticeManagement.getFiles().addAll(files);

    return NoticeDetailResponse.fromEntity(noticeManagement);
  }

  // 클라이언트용: 활성화된 공지사항만 조회
  @Description("클라이언트용 공지 상세 조회 (활성화되고 삭제되지 않은 것만)")
  @Transactional(readOnly = true)
  public ClientNoticeDetailResponse readClientDetailNotice(Long id) {
    NoticeManagement noticeManagement = noticeRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id)
        .orElseThrow(() -> new BaseException(404, "해당 공지사항을 찾을 수 없습니다"));

    // 파일들을 명시적으로 로딩 (LAZY 로딩 해결)
    List<NoticeFile> files = noticeFileRepository.findByNoticeManagementId(id);
    noticeManagement.getFiles().clear();
    noticeManagement.getFiles().addAll(files);

    return ClientNoticeDetailResponse.fromEntity(noticeManagement);
  }

  @Description("제목, 내용 기반 검색 + 카테고리 필터링")
  @Transactional(readOnly = true)
  public NoticeListResponse searchNotice(String keyword, List<String> categories, int page, int size, String sortBy) {
    int pageSize = switch (size) {
      case 30 -> 30;
      case 50 -> 50;
      default -> 10;
    };

    Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
    Pageable pageable = PageRequest.of(page, pageSize, sort);

    QNoticeManagement q = QNoticeManagement.noticeManagement;
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(q.isDeleted.isFalse());

    // 카테고리 필터링
    if (categories != null && !categories.isEmpty()) {
      builder.and(q.category.in(categories));
    }

    // 제목 / 내용 키워드 검색
    if (keyword != null && !keyword.trim().isEmpty()) {
      builder.and(
          q.title.containsIgnoreCase(keyword)
              .or(q.content.containsIgnoreCase(keyword)));
    }

    List<NoticeManagement> notices = jpaQueryFactory.selectFrom(q)
        .where(builder)
        .orderBy("createdAt".equals(sortBy) ? q.createdAt.desc() : q.updatedAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = jpaQueryFactory.selectFrom(q).where(builder).fetchCount();

    List<NoticeAllResponse> responseList = notices.stream()
        .map(n -> {
          int fileCount = noticeFileRepository.findByNoticeManagementId(n.getId()).size();
          return new NoticeAllResponse(
              n.getId(), n.getCategory(), n.getTitle(), n.getIsActive(), n.getCreatedAt(), fileCount);
        })
        .toList();

    return new NoticeListResponse(
        responseList,
        total,
        pageable.getPageNumber(),
        pageable.getPageSize(),
        (int) Math.ceil((double) total / pageable.getPageSize()));
  }

  @Transactional
  public void updateNotice(Long id, String category, String title, String content, Boolean isActive,
      List<Long> deleteFileIds, List<MultipartFile> files) {
    // Notice 엔티티 조회 및 업데이트
    NoticeManagement notice = noticeRepository.findByIdAndIsDeletedIsFalse(id)
        .orElseThrow(() -> new BaseException(404, "해당 공지사항을 찾을 수 없습니다"));

    // 입력 값 검증
    if (category == null || category.trim().isEmpty()) {
      throw new BaseException(400, "카테고리 설정은 필수입니다.");
    }
    if (title == null || title.trim().isEmpty()) {
      throw new BaseException(400, "제목 작성은 필수입니다.");
    }
    if (content == null || content.trim().isEmpty()) {
      throw new BaseException(400, "내용 작성은 필수입니다.");
    }
    if (isActive == null) {
      throw new BaseException(400, "상태 설정은 필수입니다.");
    }

    // Notice 정보 업데이트
    NoticeCreateRequest updateRequest = new NoticeCreateRequest(
        category.trim(), title.trim(), content.trim(), isActive, files);
    notice.updateNoticeManagement(updateRequest);
    noticeRepository.save(notice);

    // 선택적 파일 삭제 및 새 파일 추가
    updateFilesSelectively(id, deleteFileIds, files, notice);
  }

  private void replaceAllFiles(Long noticeId, List<MultipartFile> newFiles, NoticeManagement notice) {
    // 1. 기존 파일들 모두 삭제 (S3 + DB)
    List<NoticeFile> existingFiles = noticeFileRepository.findByNoticeManagementId(noticeId);

    for (NoticeFile existingFile : existingFiles) {
      // S3에서 파일 삭제
      s3Service.deleteFile(existingFile.getFilePath());
    }

    // DB에서 기존 파일 레코드들 모두 삭제
    noticeFileRepository.deleteByNoticeManagementId(noticeId);

    // 2. 새로운 파일들 업로드 및 저장
    if (newFiles != null && !newFiles.isEmpty()) {
      uploadAndSaveFiles(newFiles, notice);
      log.info("새로운 파일 업로드 완료");
    } else {
      log.info("새로운 파일이 없음 - newFiles: {}", newFiles);
    }
  }

  private void updateFilesSelectively(Long noticeId, List<Long> deleteFileIds, List<MultipartFile> newFiles,
      NoticeManagement notice) {
    log.info("선택적 파일 업데이트 시작 - Notice ID: {}, 삭제할 파일 ID: {}, 새 파일 개수: {}",
        noticeId, deleteFileIds, newFiles != null ? newFiles.size() : 0);

    // 1. 삭제할 파일들 처리
    if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
      for (Long fileId : deleteFileIds) {
        // 해당 파일이 이 notice의 파일인지 검증
        NoticeFile fileToDelete = noticeFileRepository.findById(fileId)
            .orElseThrow(() -> new BaseException(404, "삭제하려는 파일을 찾을 수 없습니다. 파일 ID: " + fileId));

        if (!fileToDelete.getNoticeManagement().getId().equals(noticeId)) {
          throw new BaseException(400, "해당 공지사항의 파일이 아닙니다. 파일 ID: " + fileId);
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
        noticeFileRepository.delete(fileToDelete);
        log.info("DB에서 파일 레코드 삭제 완료 - 파일 ID: {}", fileId);
      }
    }

    // 2. 새로운 파일들 업로드 및 저장
    if (newFiles != null && !newFiles.isEmpty()) {
      uploadAndSaveFiles(newFiles, notice);
      log.info("새로운 파일들 업로드 완료 - 개수: {}", newFiles.size());
    }

    log.info("선택적 파일 업데이트 완료");
  }

  @Description("공지 삭제 - Notice는 soft delete, 파일은 실제 삭제")
  @Transactional
  public void deleteNotice(@PathVariable("id") Long id) {
    NoticeManagement notice = noticeRepository.findByIdAndIsDeletedIsFalse(id)
        .orElseThrow(() -> new BaseException(400, "해당 공지사항을 찾을 수 없습니다"));

    // Notice에 연결된 파일들을 실제 삭제 (S3 + DB)
    List<NoticeFile> files = noticeFileRepository.findByNoticeManagementId(id);

    log.info("Notice {} 삭제 시작 - 연결된 파일 수: {}", id, files.size());

    // S3에서 파일들 실제 삭제
    int deletedFileCount = 0;
    int failedFileCount = 0;

    for (NoticeFile file : files) {
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

    // DB에서 NoticeFile 레코드들 모두 삭제
    if (!files.isEmpty()) {
      noticeFileRepository.deleteByNoticeManagementId(id);
      log.info("DB에서 NoticeFile 레코드들 삭제 완료");
    }

    // Notice soft delete
    notice.deleteNoticeManagement();
    noticeRepository.save(notice);

    log.info("Notice {} 삭제 완료 - S3 파일 삭제 성공: {}, 실패: {}", id, deletedFileCount, failedFileCount);
  }

  // 클라이언트용: 활성화된 공지사항 전체 조회 (카테고리 필터링 포함)
  @Description("클라이언트용 공지사항 전체 조회 (활성화되고 삭제되지 않은 것만)")
  @Transactional(readOnly = true)
  public ClientNoticeListResponse readAllClientNotice(List<String> categories, int page, int size, String sortBy) {
    int pageSize = switch (size) {
      case 30 -> 30;
      case 50 -> 50;
      default -> 10;
    };

    Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
    Pageable pageable = PageRequest.of(page, pageSize, sort);
    Page<NoticeManagement> pageResult;

    if (categories == null || categories.isEmpty()) {
      pageResult = noticeRepository.findAllByIsActiveTrueAndIsDeletedFalse(pageable);
    } else {
      pageResult = noticeRepository.findAllByCategoryInAndIsActiveTrueAndIsDeletedFalse(categories, pageable);
    }

    List<ClientNoticeAllResponse> responseList = pageResult.getContent().stream()
        .map(notice -> {
          int fileCount = noticeFileRepository.findByNoticeManagementId(notice.getId()).size();
          return new ClientNoticeAllResponse(
              notice.getId(),
              notice.getCategory(),
              notice.getTitle(),
              notice.getCreatedAt(),
              fileCount);
        })
        .toList();

    return new ClientNoticeListResponse(
        responseList,
        pageResult.getTotalElements(),
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalPages());
  }

  // 클라이언트용: 활성화된 공지사항 키워드 기반 검색
  @Description("클라이언트용 공지사항 검색 (활성화되고 삭제되지 않은 것만)")
  @Transactional(readOnly = true)
  public ClientNoticeListResponse searchClientNotice(String keyword, List<String> categories, int page, int size,
      String sortBy) {
    int pageSize = switch (size) {
      case 30 -> 30;
      case 50 -> 50;
      default -> 10;
    };

    Sort sort = Sort.by("createdAt".equals(sortBy) ? "createdAt" : "updatedAt").descending();
    Pageable pageable = PageRequest.of(page, pageSize, sort);

    QNoticeManagement q = QNoticeManagement.noticeManagement;
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(q.isDeleted.isFalse());
    builder.and(q.isActive.isTrue()); // 클라이언트용: 활성화된 공지사항만

    // 카테고리 필터링
    if (categories != null && !categories.isEmpty()) {
      builder.and(q.category.in(categories));
    }

    // 제목 / 내용 키워드 검색
    if (keyword != null && !keyword.trim().isEmpty()) {
      builder.and(
          q.title.containsIgnoreCase(keyword)
              .or(q.content.containsIgnoreCase(keyword)));
    }

    List<NoticeManagement> notices = jpaQueryFactory.selectFrom(q)
        .where(builder)
        .orderBy("createdAt".equals(sortBy) ? q.createdAt.desc() : q.updatedAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = jpaQueryFactory.selectFrom(q).where(builder).fetchCount();

    List<ClientNoticeAllResponse> responseList = notices.stream()
        .map(n -> {
          int fileCount = noticeFileRepository.findByNoticeManagementId(n.getId()).size();
          return new ClientNoticeAllResponse(
              n.getId(),
              n.getCategory(),
              n.getTitle(),
              n.getCreatedAt(),
              fileCount);
        })
        .toList();

    return new ClientNoticeListResponse(
        responseList,
        total,
        pageable.getPageNumber(),
        pageable.getPageSize(),
        (int) Math.ceil((double) total / pageable.getPageSize()));
  }

}
