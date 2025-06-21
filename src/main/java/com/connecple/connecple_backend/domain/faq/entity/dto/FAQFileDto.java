package com.connecple.connecple_backend.domain.faq.entity.dto;

import com.connecple.connecple_backend.domain.faq.entity.FAQFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FAQFileDto {
    private Long id;
    private String originalFileName;
    private String filePath;
    private Long fileSize;
    private String fileType;

    public static FAQFileDto fromEntity(FAQFile faqFile) {
        return new FAQFileDto(
                faqFile.getId(),
                faqFile.getOriginalFileName(),
                faqFile.getFilePath(),
                faqFile.getFileSize(),
                faqFile.getFileType());
    }
}