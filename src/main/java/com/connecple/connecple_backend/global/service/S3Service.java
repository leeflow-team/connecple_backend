package com.connecple.connecple_backend.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.connecple.connecple_backend.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = folder + "/" + UUID.randomUUID().toString() + fileExtension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata);
            amazonS3.putObject(request);

            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new BaseException(500, "Failed to upload file to S3: " + e.getMessage());
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            // URL에서 S3 키 추출
            String key = extractKeyFromUrl(fileUrl);
            if (key != null) {
                DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
                amazonS3.deleteObject(deleteObjectRequest);
            }
        } catch (Exception e) {
            // 삭제 실패 시 로그만 남기고 예외를 던지지 않음 (이미 삭제된 파일일 수 있음)
            log.error("Failed to delete file from S3: {}, Error: {}", fileUrl, e.getMessage());
        }
    }

    private String extractKeyFromUrl(String fileUrl) {
        try {
            // URL에서 bucket name 이후의 경로를 추출
            String bucketUrl = "https://" + bucketName + ".s3." + amazonS3.getRegionName() + ".amazonaws.com/";
            if (fileUrl.startsWith(bucketUrl)) {
                return fileUrl.substring(bucketUrl.length());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}