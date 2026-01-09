package com.example.backend.dto.request;

import com.example.backend.domain.chat.file.FileInfo;
import com.example.backend.domain.constant.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest { // (클라이언트가 보내는 데이터)
    private String roomId;
    private String content;
    private MessageType type;
    private FileInfo fileInfo; // 파일이 있을 때만 채워서 보냄
}