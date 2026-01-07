package com.example.backend.controller;

import com.example.backend.domain.chat.ChatLog;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @대화 내역 조회 [STEP2]
// [STEP 2]데이터 제공: ChatApiController 작성
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    // 특정 채팅방의 과거 메시지 목록 조회
    @GetMapping("/room/{roomId}")
    public List<ChatLog> getHistory(@PathVariable String roomId) {
        return chatService.getChatHistory(roomId);
    }
}

/*
[STEP 3] 백엔드 개발자로서의 관전 포인트
이 API가 추가됨으로써 유저님의 프로젝트는 다음과 같은 **클라이언트-서버 프로그래밍(Client-Server Programming)**의 정석적인 흐름을 갖추게 되었습니다:

입장 시 (HTTP): GET /api/chat/room/{roomId}를 호출하여 MongoDB에 저장된 과거 데이터를 먼저 화면에 뿌려줍니다.

입장 후 (WebSocket): /ws-stomp를 통해 실시간 연결을 맺고, 이후 발생하는 실시간 데이터를 수신합니다.

퇴장 후 재입장: 다시 1번 과정이 반복되며 데이터의 연속성이 보장됩니다.
 */