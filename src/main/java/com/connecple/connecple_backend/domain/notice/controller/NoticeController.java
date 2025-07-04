package com.connecple.connecple_backend.domain.notice.controller;

import com.connecple.connecple_backend.domain.notice.dto.req.NoticeCreateRequest;
import com.connecple.connecple_backend.domain.notice.dto.req.NoticeUpdateRequest;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeAllResponse;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeDetailResponse;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeListResponse;
import com.connecple.connecple_backend.domain.notice.service.NoticeService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeController {

  private final NoticeService noticeService;

  @Description("공지 생성 (JSON)")
  @PostMapping
  public ResponseEntity<SuccessResponse<Void>> createNotice(HttpSession session,
      @RequestBody @Valid NoticeCreateRequest noticeCreateRequest) {
    checkAdmin(session);
    noticeService.createNotice(noticeCreateRequest);
    return ResponseEntity.ok().body(SuccessResponse.success());
  }

  @Description("공지 생성 (파일 업로드 포함)")
  @PostMapping(consumes = { "multipart/form-data" })
  public ResponseEntity<SuccessResponse<Void>> createNoticeWithFiles(
      HttpSession session,
      @RequestParam("category") String category,
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam("isActive") Boolean isActive,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) {
    checkAdmin(session);

    noticeService.createNotice(category, title, content, isActive, files);
    return ResponseEntity.ok(SuccessResponse.success());
  }

  @Description("공지 전체 조회 (다중 카테고리 필터링 포함)")
  @GetMapping
  public ResponseEntity<SuccessResponse<NoticeListResponse>> readAllNotice(HttpSession session,
      @RequestParam(name = "category", required = false) List<String> categories,
      @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    checkAdmin(session);
    return ResponseEntity.ok().body(SuccessResponse.success(
        noticeService.readAllNotice(categories, page, size, sortBy)));
  }

  @Description("공지 세부 조회")
  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<NoticeDetailResponse>> readDetailNotice(HttpSession session,
      @PathVariable("id") Long id) {
    checkAdmin(session);
    return ResponseEntity.ok().body(SuccessResponse.success(noticeService.readDetailNotice(id)));
  }

  @Description("공지 수정 (파일 업로드 포함)")
  @PatchMapping(value = "/{id}", consumes = { "multipart/form-data" })
  public ResponseEntity<SuccessResponse<Void>> updateNoticeWithFiles(
      HttpSession session,
      @PathVariable Long id,
      @RequestParam("category") String category,
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam("isActive") Boolean isActive,
      @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) {
    checkAdmin(session);

    noticeService.updateNotice(id, category, title, content, isActive, deleteFileIds, files);
    return ResponseEntity.ok(SuccessResponse.success());
  }

  @Description("공지 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteDetailNotice(HttpSession session,
      @PathVariable("id") Long id) {
    checkAdmin(session);
    noticeService.deleteNotice(id);
    return ResponseEntity.ok().body(SuccessResponse.success());
  }

  @Description("공지 키워드 기반 검색")
  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<NoticeListResponse>> searchNotice(
      HttpSession session,
      @RequestParam("keyword") String keyword,
      @RequestParam(name = "category", required = false) List<String> categories,
      @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

    checkAdmin(session);
    NoticeListResponse result = noticeService.searchNotice(keyword, categories, page, size, sortBy);
    return ResponseEntity.ok().body(SuccessResponse.success(result));
  }

}
