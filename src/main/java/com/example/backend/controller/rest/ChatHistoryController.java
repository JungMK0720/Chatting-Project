package com.example.backend.controller.rest;

import com.example.backend.domain.chat.ChatMessage;
import com.example.backend.dto.response.ChatMessageResponse;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatService chatService;

    /**
     * [GET] 특정 채팅방의 과거 메시지 조회
     * 방을 클릭하고 들어갔을 때, MongoDB의 데이터를 쫘악 가져와 뿌려줍니다.
     * 페이징 처리를 추가하면 카톡처럼 '스크롤 올릴 때 데이터 로딩'이 가능해집니다.
     */
    @GetMapping("/{roomId}/messages")
    public List<ChatMessageResponse> getHistory(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page) {
        // 반환 타입을 List<ChatMessageResponse>로 일치시킵니다.
        return chatService.getChatHistory(roomId, page);
    }

    @GetMapping("/{roomId}/messages")
    public List<ChatMessageResponse> getHistory(@PathVariable String roomId) {
        return chatService.getChatHistory(roomId);
    }
}