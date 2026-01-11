package com.example.backend.controller.api.file;

import com.example.backend.domain.chat.file.FileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat/file")
@RequiredArgsConstructor
public class FileApiController {

    @PostMapping("/upload")
    public FileInfo uploadFile(@RequestParam("file") MultipartFile file) {
        // 1. 파일을 로컬이나 S3에 저장하는 로직 수행
        // 2. 저장된 결과(URL 등)를 바탕으로 FileInfo 빌드
        return FileInfo.builder()
                .fileUrl("http://storage.com/test.png")
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
    }
}