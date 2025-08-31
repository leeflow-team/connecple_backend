package com.connecple.connecple_backend.domain.program.service;

import com.connecple.connecple_backend.domain.program.dto.ProgramDto;
import com.connecple.connecple_backend.domain.program.dto.ProgramFileDto;
import com.connecple.connecple_backend.domain.program.entity.Program;
import com.connecple.connecple_backend.domain.program.entity.ProgramFile;
import com.connecple.connecple_backend.domain.program.entity.request.ProgramBulkSaveRequest;
import com.connecple.connecple_backend.domain.program.entity.request.ProgramFileBulkSaveRequest;
import com.connecple.connecple_backend.domain.program.repository.ProgramFileRepository;
import com.connecple.connecple_backend.domain.program.repository.ProgramRepository;
import com.connecple.connecple_backend.global.exception.BaseException;
import com.connecple.connecple_backend.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class ProgramService {
    private final ProgramRepository programRepository;
    private final ProgramFileRepository programFileRepository;
    private final S3Service s3Service;

    public List<ProgramDto> getProgramList() {
        List<ProgramDto> programList = programRepository.getProgramList();
        return programList;
    }

    public List<ProgramFileDto> getProgramFileList(Long programId) {
        return programFileRepository.getProgramFileList(programId);
    }

    @Transactional
    public void resetProgramFiles(ProgramFileBulkSaveRequest request) {
        Long programId = request.getProgramId();
        List<MultipartFile> images = request.getImages();

        if (images.isEmpty()) {
            throw new BaseException(400, "이미지, content1, content2, content3 목록은 필수입니다");
        }

        if (images.size() > 4) {
            throw new BaseException(400, "최대 4개까지 업로드 할 수 있습니다.");
        }

        // 모든 이미지 파일 검증
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            validateImageFile(image, i + 1);
        }

        // 기존 데이터의 S3 파일들 삭제
        List<ProgramFile> existingProgramFiles = programFileRepository.findAll();
        for (ProgramFile programFile : existingProgramFiles) {
            s3Service.deleteFile(programFile.getImagePath());
        }

        programFileRepository.deleteAllByProgramId(programId);

        // 새로운 데이터 순서대로 저장
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);

            // S3에 업로드
            String imagePath = s3Service.uploadFile(image, "program-file-images");

            ProgramFile programFile = ProgramFile.builder()
                    .programId(programId)
                    .sortOrder((long) (i + 1))
                    .imagePath(imagePath)
                    .build();

            programFileRepository.save(programFile);
        }
    }

    @Transactional
    public void resetPrograms(ProgramBulkSaveRequest request) {
        List<String> levels = request.getLevels();
        List<MultipartFile> images = request.getImages();
        List<String> content1List = request.getContents1();
        List<String> content2List = request.getContents2();
        List<String> content3List = request.getContents3();

        if (images.isEmpty() || content1List.isEmpty() || content2List.isEmpty() || content3List.isEmpty() || levels.isEmpty()) {
            throw new BaseException(400, "레벨, 이미지, content1, content2, content3 목록은 필수입니다");
        }

        if (images.size() != content1List.size() || images.size() != content2List.size() || images.size() != content3List.size() || images.size() != levels.size()) {
            throw new BaseException(400, "레벨, 이미지, content1, content2, content3 목록의 크기가 일치해야 합니다");
        }

        if (images.size() > 5) {
            throw new BaseException(400, "최대 5개까지 업로드 할 수 있습니다.");
        }

        // 모든 이미지 파일 검증
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            validateImageFile(image, i + 1);
        }

        // 기존 데이터의 S3 파일들 삭제
        List<Program> existingPrograms = programRepository.findAll();
        for (Program program : existingPrograms) {
            s3Service.deleteFile(program.getImagePath());
        }

        programRepository.deleteAll();

        // 새로운 데이터 순서대로 저장
        for (int i = 0; i < images.size(); i++) {
            String level = levels.get(i);
            MultipartFile image = images.get(i);
            String content1 = content1List.get(i);
            String content2 = content2List.get(i);
            String content3 = content3List.get(i);

            // S3에 업로드
            String imagePath = s3Service.uploadFile(image, "program-images");

            Program program = Program.builder()
                    .imagePath(imagePath)
                    .sortOrder((long) (i + 1)) // 1부터 시작하는 순서
                    .level(level)
                    .content1(content1)
                    .content2(content2)
                    .content3(content3)
                    .build();

            programRepository.save(program);
        }
    }

    private void validateImageFile(MultipartFile imageFile, int index) {
        // 파일이 비어있는지 검증
        if (imageFile == null || imageFile.isEmpty()) {
            throw new BaseException(400, index + "번째 이미지 파일이 비어있습니다.");
        }

        // 파일명 검증
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BaseException(400, index + "번째 이미지 파일명이 없습니다.");
        }

        // 파일 크기 검증 (10MB 제한)
        if (imageFile.getSize() > 10 * 1024 * 1024) {
            throw new BaseException(413, index + "번째 이미지 파일 크기는 10MB를 초과할 수 없습니다.");
        }

        // 파일 확장자 검증 (jpg, jpeg, png만 허용)
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png")) {
            throw new BaseException(415, index + "번째 파일은 이미지 파일만 업로드 가능합니다. (jpg, jpeg, png만 허용)");
        }

        // MIME 타입 검증 (추가 보안)
        String contentType = imageFile.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/jpg")
                        && !contentType.equals("image/png"))) {
            throw new BaseException(400, index + "번째 파일은 이미지 파일만 업로드 가능합니다. (jpg, jpeg, png만 허용)");
        }

        log.info("{}번째 이미지 파일 검증 완료: {} (크기: {} bytes, 타입: {})",
                index, originalFilename, imageFile.getSize(), contentType);
    }
}
