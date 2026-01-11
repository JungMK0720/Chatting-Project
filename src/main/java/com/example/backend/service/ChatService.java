package com.example.backend.service;

import com.example.backend.domain.chat.ChatMessage;
import com.example.backend.domain.constant.MessageType;
import com.example.backend.dto.request.ChatMessageRequest;
import com.example.backend.dto.request.ChatRoomRequest;
import com.example.backend.dto.response.ChatMessageResponse;
import com.example.backend.dto.response.ChatRoomResponse;
import com.example.backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// @대화내역 조회 [STEP1]
// [STEP 1] 로직의 분리: ChatService 작성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 특정 방의 과거 대화 내역을 가져옵니다.
     *
     * @param roomId 채팅방 ID
     * @return 시간순으로 정렬된 대화 로그 리스트
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }

    // 페이징 처리 버전 (에러 해결용)
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(String roomId, int page) {
        // 실제로는 Pageable을 사용하여 쿼리해야 하지만, 우선 컴파일 에러 해결을 위해 작성
        return getChatHistory(roomId);
    }

    // ChatRoomController를 위한 스텁(Stub) 메서드들
    public List<ChatRoomResponse> findAllRoomsByUserId(String userId) {
        // PostgreSQL에서 유저가 참여 중인 방 목록 조회 로직 구현 필요
        return List.of();
    }

    public ChatRoomResponse createChatRoom(ChatRoomRequest request, String userId) {
        // 방 생성 로직 구현 필요
        return ChatRoomResponse.builder().build();
    }

    public void leaveRoom(String roomId, String userId) {
        // 방 나가기 로직 구현 필요
    }

    @Transactional // 저장 로직이므로 readOnly 제외
    public ChatMessageResponse processMessage(ChatMessageRequest request, String userId, String nickname) {
        // 이제 .fileInfo() 메서드를 정상적으로 호출할 수 있습니다.
        ChatMessage messageEntity = ChatMessage.builder()
                .roomId(request.getRoomId())
                .senderId(userId)
                .senderNickname(nickname)
                .content(request.getContent())
                .type(request.getType())
                .fileInfo(request.getType() == MessageType.FILE ? request.getFileInfo() : null)
                .build();

        return ChatMessageResponse.from(chatMessageRepository.save(messageEntity));
    }
}