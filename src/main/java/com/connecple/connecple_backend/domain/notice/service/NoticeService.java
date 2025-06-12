package com.connecple.connecple_backend.domain.notice.service;

import com.connecple.connecple_backend.domain.notice.dto.req.NoticeCreateRequest;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeAllResponse;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeDetailResponse;
import com.connecple.connecple_backend.domain.notice.dto.res.NoticeListResponse;
import com.connecple.connecple_backend.domain.notice.entity.NoticeManagement;
import com.connecple.connecple_backend.domain.notice.entity.QNoticeManagement;
import com.connecple.connecple_backend.domain.notice.repository.NoticeRepository;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import com.connecple.connecple_backend.global.exception.BaseException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Description("공지 생성")
  public void createNotice(NoticeCreateRequest request){
    NoticeManagement noticeManagement = new NoticeManagement(request.getCategory(), request.getTitle(),
        request.getContent(), request.getIsActive());

    noticeRepository.save(noticeManagement);
  }

  @Description("공지 전체 조회 (페이지네이션, 10, 30, 50 개씩 보기 가능, 삭제된 것은 조회 안됨, 전체 개수도 카운트해서 반환)")
  @Transactional(readOnly = true)
  public NoticeListResponse readAllNotice(int page, int size, String sortBy){
    // 페이지 크기 제한
    int pageSize = switch (size) {
      case 30 -> 30;
      case 50 -> 50;
      default -> 10;
    };

    // 정렬 조건 설정
    Sort sort = Sort.by(
        "createdAt".equals(sortBy) ? "createdAt" : "updatedAt"
    ).descending();

    Pageable pageable = PageRequest.of(page, pageSize, sort);

    Page<NoticeManagement> noticeManagementPage = noticeRepository.findAllByIsDeletedIsFalse(pageable);

    // NoticeManagement -> NoticeAllResponse 변환
    List<NoticeAllResponse> responseList = noticeManagementPage.getContent().stream()
        .map(noticeManagement -> new NoticeAllResponse(
            noticeManagement.getId(),
            noticeManagement.getCategory(),
            noticeManagement.getTitle(),
            noticeManagement.getIsActive(),
            noticeManagement.getCreatedAt()
        ))
        .toList();

    // Custom response with total count
    return new NoticeListResponse(
        responseList,
        noticeManagementPage.getTotalElements(), // Total count
        noticeManagementPage.getNumber(),
        noticeManagementPage.getSize(),
        noticeManagementPage.getTotalPages()
    );

  }

  @Description("공지 상세 조회 (삭제된 것은 조회 안됨)")
  @Transactional(readOnly = true)
  public NoticeDetailResponse readDetailNotice(@PathVariable("id") Long id){
    NoticeManagement noticeManagement = noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> new BaseException(400, "해당 공지사항을 찾을 수 없습니다"));
    return NoticeDetailResponse.fromEntity(noticeManagement);
  }

 @Description("제목, 카테고리, 내용 기반 검색, (페이지네이션, 10, 30, 50 개씩 보기 가능, 삭제된 것은 조회 안됨, 전체 개수도 카운트해서 반환)")
  public Page<NoticeAllResponse> searchNotice(String keyword, int page, int size, String sortBy){
    // 페이지 크기 제한
    int pageSize = switch (size) {
      case 30 -> 30;
      case 50 -> 50;
      default -> 10;
    };

    // 정렬 조건 설정
    Sort sort = Sort.by(
        "createdAt".equals(sortBy) ? "createdAt" : "updatedAt"
    ).descending();

    Pageable pageable = PageRequest.of(page, pageSize, sort);

   QNoticeManagement qNoticeManagement = QNoticeManagement.noticeManagement;
   BooleanBuilder builder = new BooleanBuilder();

   // 삭제되지 않은 공지만 조회
   builder.and(qNoticeManagement.isDeleted.isFalse());

   // 키워드가 null 또는 빈 문자열이 아닌 경우 검색 조건 추가
   if (keyword != null && !keyword.trim().isEmpty()) {
     builder.and(
         qNoticeManagement.title.containsIgnoreCase(keyword)
             .or(qNoticeManagement.category.containsIgnoreCase(keyword))
             .or(qNoticeManagement.content.containsIgnoreCase(keyword))
     );
   }

   // QueryDSL 쿼리 실행
   List<NoticeManagement> noticeList = jpaQueryFactory.selectFrom(qNoticeManagement)
       .where(builder)
       .orderBy("createdAt".equals(sortBy) ?
           qNoticeManagement.createdAt.desc() : qNoticeManagement.updatedAt.desc())
       .offset(pageable.getOffset())
       .limit(pageable.getPageSize())
       .fetch();

   // 전체 개수 카운트
   long total = jpaQueryFactory
       .selectFrom(qNoticeManagement)
       .where(builder)
       .fetchCount();

   // NoticeManagement -> NoticeAllResponse 변환
   List<NoticeAllResponse> responseList = noticeList.stream()
       .map(noticeManagement -> new NoticeAllResponse(
           noticeManagement.getId(),
           noticeManagement.getCategory(),
           noticeManagement.getTitle(),
           noticeManagement.getIsActive(),
           noticeManagement.getCreatedAt()
       ))
       .toList();

   // Page 객체로 반환
   return new PageImpl<>(responseList, pageable, total);

  }

  @Description("공지 수정, 삭제된 것은 수정 안됨")
  @Transactional
  public void updateNotice(Long id, NoticeCreateRequest request){
    NoticeManagement notice = noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> new BaseException(400, "해당 공지사항을 찾을 수 없습니다"));

    //공지 업데이트
    notice.updateNoticeManagement(request);

    noticeRepository.save(notice);
  }

  @Description("공지 삭제, 삭제된 것은 또 삭제 안됨")
  @Transactional
  public void deleteNotice(@PathVariable("id") Long id){
    NoticeManagement notice = noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> new BaseException(400, "해당 공지사항을 찾을 수 없습니다"));

    notice.deleteNoticeManagement();

    noticeRepository.save(notice);
  }


}
