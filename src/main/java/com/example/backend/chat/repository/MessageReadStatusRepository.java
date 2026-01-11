package com.example.backend.chat.repository;

import com.example.backend.chat.entity.ChatMessage;
import com.example.backend.chat.entity.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long> {

    /**
     * 특정 메시지를 읽은 사람의 수를 카운트합니다.
     * (안 읽은 수 = 총 인원 - 이 값)
     */
    long countByChatMessage(ChatMessage chatMessage);
}