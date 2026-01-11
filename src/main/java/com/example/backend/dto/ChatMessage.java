package com.example.backend.dto;

import com.example.backend.domain.chat.file.FileInfo;
import com.example.backend.domain.constant.MessageType;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder // 빌더 패턴을 쓰면 객체 생성이 편해집니다
public class ChatMessage {

    private MessageType type;

    // 1. 방 번호 (가장 중요!)
    // 서버는 이 ID를 보고 어떤 방 구독자들에게 메시지를 뿌릴지 결정합니다.
    private String roomId;

    // 2. 보내는 사람 ID
    // 닉네임(sender)도 좋지만, 고유한 User ID(Long 또는 UUID)가 있어야 DB 관리가 정확합니다.
    private String senderId;
    private String senderNickname;

    // 3. 메시지 내용
    private String content;

    // 4. 파일 정보 필드 추가
    private FileInfo fileInfo;

    // 5. 전송 시간
    // 클라이언트 UI에서 메시지 순서를 잡거나 시간을 표시할 때 필요합니다.
    private String timestamp;
}