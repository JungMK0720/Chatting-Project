package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
// (채팅방 목록 응답용)
public class ChatRoomResponse {
    private String roomId;
    private String roomName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private int participantCount;
    // private long unreadCount; // 나중에 Redis나 별도 로직으로 구현 가능
}