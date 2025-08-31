package com.connecple.connecple_backend.domain.program.controller;

import com.connecple.connecple_backend.domain.program.dto.ProgramDto;
import com.connecple.connecple_backend.domain.program.dto.ProgramFileDto;
import com.connecple.connecple_backend.domain.program.entity.request.ProgramBulkSaveRequest;
import com.connecple.connecple_backend.domain.program.entity.request.ProgramFileBulkSaveRequest;
import com.connecple.connecple_backend.domain.program.service.ProgramService;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProgramController {
    private final ProgramService programService;

    @GetMapping("/programs")
    public ResponseEntity<List<ProgramDto>> getPrograms() {
        return ResponseEntity.ok(programService.getProgramList());
    }

    @GetMapping("/programs/{programId}/files")
    public ResponseEntity<List<ProgramFileDto>> getProgramFile(@PathVariable Long programId) {
        return ResponseEntity.ok(programService.getProgramFileList(programId));
    }

    @PostMapping("/programs/files/reset")
    public ResponseEntity<SuccessResponse<String>> resetProgramFiles(
            HttpSession session,
            @RequestParam("programId") Long programId,
            @RequestParam("images") List<MultipartFile> images
    ) {
        checkAdmin(session);

        ProgramFileBulkSaveRequest request = new ProgramFileBulkSaveRequest(programId, images);

        programService.resetProgramFiles(request);
        return ResponseEntity.ok(new SuccessResponse<>("Main intro images have been successfully reset", "success"));
    }

    @PostMapping("/programs/reset")
    public ResponseEntity<SuccessResponse<String>> resetProgram(
            HttpSession session,
            @RequestParam("level") List<String> levels,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("content1") List<String> contents1,
            @RequestParam("content2") List<String> contents2,
            @RequestParam("content3") List<String> contents3
            ) {
        checkAdmin(session);

        ProgramBulkSaveRequest request = new ProgramBulkSaveRequest(levels, images, contents1, contents2, contents3);

        programService.resetPrograms(request);
        return ResponseEntity.ok(new SuccessResponse<>("Main intro images have been successfully reset", "success"));
    }
}
