package com.connecple.connecple_backend.domain.news.controller;

import com.connecple.connecple_backend.domain.news.dto.NewsDto;
import com.connecple.connecple_backend.domain.news.entity.request.NewsBulkSaveRequest;
import com.connecple.connecple_backend.domain.news.service.NewsService;
import com.connecple.connecple_backend.global.common.LoginChecker;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.connecple.connecple_backend.global.common.LoginChecker.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NewsController {
    private NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<List<NewsDto>> getNewsList() {
        return ResponseEntity.ok(newsService.getNewsList());
    }

    // 뉴스 새로 저장 (순서까지 저장)
    @PostMapping("/news/reset")
    public ResponseEntity<SuccessResponse<String>> resetNews(
            HttpSession session,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("titles") List<String> titles,
            @RequestParam("contents") List<String> contents,
            @RequestParam("newsUrlList") List<String> newsUrlList
    ) {
        checkAdmin(session);

        NewsBulkSaveRequest request = new NewsBulkSaveRequest(images, titles, contents, newsUrlList);

        newsService.resetNewsInfo(request);

        return ResponseEntity.ok(new SuccessResponse<>("News have been successfully reset", "success"));
    }
}
