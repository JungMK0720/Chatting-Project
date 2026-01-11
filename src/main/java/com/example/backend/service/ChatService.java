package com.example.backend.service;

import com.example.backend.domain.chat.ChatMessage;
import com.example.backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// @대화내역 조회 [STEP1]
// [STEP 1] 로직의 분리: ChatService 작성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 특정 방의 과거 대화 내역을 가져옵니다.
     * @param roomId 채팅방 ID
     * @return 시간순으로 정렬된 대화 로그 리스트
     */
    public List<ChatMessage> getChatHistory(String roomId) {
        // 이전에 Repository에 만들어둔 쿼리 메서드를 사용합니다.
        return chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
    }
}