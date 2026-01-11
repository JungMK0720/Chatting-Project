package com.example.backend.controller.rest;

import com.example.backend.dto.request.ChatRoomRequest;
import com.example.backend.dto.response.ChatRoomResponse;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    /**
     * [GET] 내가 참여 중인 채팅방 목록 조회
     * 모바일 앱 메인 탭의 '채팅' 리스트를 구성할 때 사용합니다.
     */
    @GetMapping
    public List<ChatRoomResponse> getMyRooms(Principal principal) {
        String userId = principal.getName(); // JWT 필터가 넣어준 유저 ID
        return chatService.findAllRoomsByUserId(userId);
    }

    /**
     * [POST] 새로운 채팅방 생성
     * 친구를 선택해서 '확인'을 눌렀을 때 호출됩니다.
     */
    @PostMapping
    public ChatRoomResponse createRoom(@RequestBody ChatRoomRequest request, Principal principal) {
        return chatService.createChatRoom(request, principal.getName());
    }

    /**
     * [DELETE] 채팅방 나가기
     * 유저가 방을 나갈 때 기록을 삭제하거나 상태를 변경합니다.
     */
    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable String roomId, Principal principal) {
        chatService.leaveRoom(roomId, principal.getName());
        return ResponseEntity.ok().build();
    }
}