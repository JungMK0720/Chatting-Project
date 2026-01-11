package com.example.backend.chat.repository; // 경로는 알맞게 수정하세요

import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomType;
import com.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /**
     * [핵심] 1:1 채팅방의 유일성을 보장하기 위한 조회 메서드
     * 정렬된 두 사용자(user1, user2)와 타입을 기준으로 기존 방을 찾습니다.
     */
    Optional<ChatRoom> findByUser1AndUser2AndType(User user1, User user2, ChatRoomType type);
}