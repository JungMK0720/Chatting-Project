package com.example.backend.domain.chat;

import com.example.backend.domain.chat.file.FileInfo;
import com.example.backend.domain.constant.MessageType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

// Messenger 기억 장치 [STEP1]
// [STEP 1] MongoDB 전용 엔티티 설계 (ChatLog)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_logs") // MongoDB의 컬렉션(테이블 역할) 이름
public class ChatMessage {

    @Id
    private String id; // MongoDB는 기본적으로 String 타입의 ObjectId를 사용합니다.
    private String roomId;
    private String senderId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private FileInfo fileInfo; // 파일 정보 (null 허용)
    private LocalDateTime createdAt; // 저장 시점의 정확한 서버 시간

    @Builder // 모든 필드를 포함하도록 생성자 수정
    public ChatMessage(String roomId, String senderId, String senderNickname, String content, MessageType type, FileInfo fileInfo) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.fileInfo = fileInfo;
        this.createdAt = LocalDateTime.now();
    }
}