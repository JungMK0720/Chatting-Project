package com.example.backend.domain.chat;

import com.example.backend.domain.constant.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

// Messenger 기억 장치 [STEP1]
// [STEP 1] MongoDB 전용 엔티티 설계 (ChatLog)
@Getter
@NoArgsConstructor
@Document(collection = "chat_logs") // MongoDB의 컬렉션(테이블 역할) 이름
public class ChatLog {

    @Id
    private String id; // MongoDB는 기본적으로 String 타입의 ObjectId를 사용합니다.

    private String roomId;
    private String senderId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt; // 저장 시점의 정확한 서버 시간

    @Builder
    public ChatLog(String roomId, String senderId, String senderNickname, String content, MessageType type) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }
}