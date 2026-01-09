package com.example.backend.domain.chat.file;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileInfo {
    private String fileUrl;   // S3 등 저장소 주소
    private String fileName;  // 파일 원본 이름
    private String fileType;  // image/png, application/pdf 등
    private long fileSize;    // 파일 크기
}