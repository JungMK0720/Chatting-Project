package com.example.backend.dto.response;

import com.example.backend.domain.chat.ChatMessage;
import com.example.backend.domain.chat.file.FileInfo;
import com.example.backend.domain.constant.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

// (채팅 메시지 응답용)
@Getter @Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
public class ChatMessageResponse {
    private String id;
    private String roomId;
    private String senderId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private FileInfo fileInfo;
    private String timestamp;

    public static ChatMessageResponse from(ChatMessage entity) {
        return ChatMessageResponse.builder()
                .id(entity.getId())
                .roomId(entity.getRoomId())
                .senderId(entity.getSenderId())
                .senderNickname(entity.getSenderNickname())
                .content(entity.getContent())
                .type(entity.getType())
                .fileInfo(entity.getFileInfo())
                .timestamp(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("a h:mm")))
                .build();
    }
}