package com.example.backend.repository;

import com.example.backend.domain.chat.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// [STEP 2] MongoDB 레포지토리 작성
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    // 특정 방의 대화 내역을 시간순으로 가져오기 위한 메서드
    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String roomId);
}