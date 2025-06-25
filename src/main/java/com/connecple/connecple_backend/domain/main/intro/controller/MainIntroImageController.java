package com.connecple.connecple_backend.domain.main.intro.controller;

import com.connecple.connecple_backend.domain.main.intro.entity.dto.MainIntroImageDto;
import com.connecple.connecple_backend.domain.main.intro.service.MainIntroImageService;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageCreateRequest;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageUpdateRequest;
import com.connecple.connecple_backend.domain.main.intro.entity.request.MainIntroImageBulkSaveRequest;
import com.connecple.connecple_backend.global.dto.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static com.connecple.connecple_backend.global.common.LoginChecker.checkAdmin;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MainIntroImageController {
    private final MainIntroImageService mainIntroImageService;

    @GetMapping("/main-intro-images")
    public ResponseEntity<List<MainIntroImageDto>> getMainIntroImages(HttpSession session) {
        checkAdmin(session);

        return ResponseEntity.ok(mainIntroImageService.getMainIntroImageList());
    }

    @PostMapping("/main-intro-images/reset")
    public ResponseEntity<SuccessResponse<String>> resetMainIntroImages(
            HttpSession session,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("titles") List<String> titles,
            @RequestParam("companies") List<String> companies) {
        checkAdmin(session);

        // DTO 생성
        MainIntroImageBulkSaveRequest request = new MainIntroImageBulkSaveRequest(images, titles, companies);

        mainIntroImageService.resetMainIntroImages(request);
        return ResponseEntity.ok(new SuccessResponse<>("Main intro images have been successfully reset", "success"));
    }

    @PatchMapping("/main-intro-images/{id}")
    public ResponseEntity<MainIntroImageDto> updateMainIntroImage(HttpSession session, @PathVariable Long id,
            @RequestBody MainIntroImageUpdateRequest request) {
        checkAdmin(session);

        return ResponseEntity.ok(mainIntroImageService.updateMainIntroImage(request, id));
    }

    @DeleteMapping("/main-intro-images/{id}")
    public ResponseEntity<Void> deleteMainIntroImage(HttpSession session, @PathVariable Long id) {
        checkAdmin(session);

        mainIntroImageService.deleteMainIntroImage(id);
        return ResponseEntity.noContent().build();
    }
}
