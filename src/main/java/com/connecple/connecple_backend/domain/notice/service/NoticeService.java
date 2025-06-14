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
              .map(notice -> new NoticeAllResponse(
                      notice.getId(),
                      notice.getCategory(),
                      notice.getTitle(),
                      notice.getIsActive(),
                      notice.getCreatedAt()
              ))
              .toList();

      return new NoticeListResponse(
              responseList,
              pageResult.getTotalElements(),
              pageResult.getNumber(),
              pageResult.getSize(),
              pageResult.getTotalPages()
      );
  }


    @Description("공지 상세 조회 (삭제된 것은 조회 안됨)")
  @Transactional(readOnly = true)
  public NoticeDetailResponse readDetailNotice(@PathVariable("id") Long id){
    NoticeManagement noticeManagement = noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> new BaseException(400, "해당 공지사항을 찾을 수 없습니다"));
    return NoticeDetailResponse.fromEntity(noticeManagement);
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
                            .or(q.content.containsIgnoreCase(keyword))
            );
        }

        List<NoticeManagement> notices = jpaQueryFactory.selectFrom(q)
                .where(builder)
                .orderBy("createdAt".equals(sortBy) ? q.createdAt.desc() : q.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.selectFrom(q).where(builder).fetchCount();

        List<NoticeAllResponse> responseList = notices.stream()
                .map(n -> new NoticeAllResponse(
                        n.getId(), n.getCategory(), n.getTitle(), n.getIsActive(), n.getCreatedAt()
                )).toList();

        return new NoticeListResponse(
                responseList,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                (int) Math.ceil((double) total / pageable.getPageSize())
        );
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
