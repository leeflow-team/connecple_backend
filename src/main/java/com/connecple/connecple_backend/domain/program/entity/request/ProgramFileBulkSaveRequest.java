package com.connecple.connecple_backend.domain.program.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProgramFileBulkSaveRequest {
    private Long programId;
    private List<MultipartFile> images;
}
