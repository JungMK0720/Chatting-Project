package com.example.backend.domain.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

// [STEP 2] MongoDB 레포지토리 작성
public interface ChatLogRepository extends MongoRepository<ChatLog, String> {
    // 특정 방의 대화 내역을 시간순으로 가져오기 위한 메서드
    List<ChatLog> findByRoomIdOrderByCreatedAtAsc(String roomId);
}