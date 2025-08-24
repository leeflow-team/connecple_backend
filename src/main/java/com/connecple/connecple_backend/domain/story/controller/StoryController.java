package com.connecple.connecple_backend.domain.story.controller;

import com.connecple.connecple_backend.domain.story.dto.StoryDto;
import com.connecple.connecple_backend.domain.story.entity.request.StoryBulkSaveRequest;
import com.connecple.connecple_backend.domain.story.service.StoryService;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StoryController {
    private final StoryService storyService;

    @GetMapping("/story")
    public ResponseEntity<List<StoryDto>> getStoryList() {
        return ResponseEntity.ok(storyService.getStoryList());
    }

    @PostMapping("/story/reset")
    public ResponseEntity<SuccessResponse<String>> resetStoryList(
            HttpSession session,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("titles") List<String> titles,
            @RequestParam("contents") List<String> contenst
    ) {
        checkAdmin(session);

        StoryBulkSaveRequest request = new StoryBulkSaveRequest(images, titles, contenst);

        storyService.resetStoryInfo(request);
        return ResponseEntity.ok(new SuccessResponse<>("Story have been successfully reset", "success"));
    }
}
