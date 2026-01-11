package com.example.backend.controller.websocket;

import com.example.backend.dto.request.ChatMessageRequest;
import com.example.backend.dto.response.ChatMessageResponse;
import com.example.backend.domain.constant.MessageType;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    /**
     * 클라이언트가 /app/chat/message로 메시지를 보낼 때 호출됩니다.
     */
    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageRequest request, Principal principal) {

        // 1. 보안: 토큰에서 추출한 진짜 유저 정보를 발신자로 사용합니다.
        String userId = principal.getName();
        String nickname = "용띠개발자"; // 실제로는 UserRepository 등을 통해 DB에서 가져와야 합니다.

        // 2. 입장 메시지 처리 로직
        // 클라이언트로부터 온 content가 있어도 JOIN 타입이면 서버에서 강제로 문구를 변경합니다.
        if (MessageType.JOIN.equals(request.getType())) {
            // Note: Request DTO는 Getter만 있으므로,
            // 실제 content 가공은 Service의 processMessage 내부에서 하거나
            // 별도의 로직을 거쳐야 합니다.
        }

        // 3. 서비스 레이어 호출 (메시지 가공 + MongoDB 저장 + Response DTO 생성)
        ChatMessageResponse response = chatService.processMessage(request, userId, nickname);

        // 4. 실시간 메시지 브로드캐스팅
        // 목적지: /topic/room/{roomId}
        messagingTemplate.convertAndSend("/topic/room/" + response.getRoomId(), response);
    }

    /**
     * 유저 추가(입장) 로직 (필요 시 유지)
     */
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessageRequest request, Principal principal) {
        String userId = principal.getName();
        String nickname = "용띠개발자";

        // 입장 메시지도 동일하게 processMessage를 통해 처리합니다.
        ChatMessageResponse response = chatService.processMessage(request, userId, nickname);
        messagingTemplate.convertAndSend("/topic/room/" + response.getRoomId(), response);
    }
}

//package com.example.backend.controller.websocket;
//
//import com.example.backend.domain.constant.MessageType;
//import com.example.backend.dto.request.ChatMessageRequest;
//import com.example.backend.dto.response.ChatMessageResponse;
//import com.example.backend.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//
//@Controller // WebSocket은 리턴값이 View가 아니어도 @Controller 사용 가능
//@RequiredArgsConstructor
//public class ChatMessageController {
//
//    private final ChatService chatService;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    /**
//     * [WS] 실시간 메시지 전송
//     * 클라이언트가 /app/chat/message로 메시지를 보낼 때 작동합니다.
//     */
//    @MessageMapping("/chat/message")
//    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
//        // 1. 보안: 토큰에서 추출한 진짜 유저 이름을 발신자로 설정
//        String senderId = principal.getName();
//        String senderNickName = "";
//
//        // 2. 서비스 레이어에 저장 및 가공 위임 (MongoDB 저장 등)
//        ChatMessageResponse response = chatService.processMessage(request, senderId, senderNickName);
//
//        // 3. 브로드캐스팅: 방을 구독 중인 모든 유저에게 메시지 전달
//        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
//    }
//
//    /**
//     * 클라이언트가 /app/chat/message로 메시지를 보낼 때 호출됩니다.
//     */
//    @MessageMapping("/chat/message")
//    public void message(@Payload ChatMessageRequest request, Principal principal) {
//
//        // 1. 보안: 토큰에서 추출한 진짜 유저 정보를 발신자로 사용합니다.
//        String userId = principal.getName();
//        String nickname = "용띠개발자"; // 실제로는 UserRepository 등을 통해 DB에서 가져와야 합니다.
//
//        // 2. 입장 메시지 처리 로직
//        // 클라이언트로부터 온 content가 있어도 JOIN 타입이면 서버에서 강제로 문구를 변경합니다.
//        if (MessageType.JOIN.equals(request.getType())) {
//            // Note: Request DTO는 Getter만 있으므로,
//            // 실제 content 가공은 Service의 processMessage 내부에서 하거나
//            // 별도의 로직을 거쳐야 합니다.
//        }
//
//        // 3. 서비스 레이어 호출 (메시지 가공 + MongoDB 저장 + Response DTO 생성)
//        ChatMessageResponse response = chatService.processMessage(request, userId, nickname);
//
//        // 4. 실시간 메시지 브로드캐스팅
//        // 목적지: /topic/room/{roomId}
//        messagingTemplate.convertAndSend("/topic/room/" + response.getRoomId(), response);
//    }
//
//    /**
//     * 유저 추가(입장) 로직 (필요 시 유지)
//     */
//    @MessageMapping("/chat.addUser")
//    public void addUser(@Payload ChatMessageRequest request, Principal principal) {
//        String userId = principal.getName();
//        String nickname = "용띠개발자";
//
//        // 입장 메시지도 동일하게 processMessage를 통해 처리합니다.
//        ChatMessageResponse response = chatService.processMessage(request, userId, nickname);
//        messagingTemplate.convertAndSend("/topic/room/" + response.getRoomId(), response);
//    }
//
////    @MessageMapping("/chat/message")
////    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
////        // [보안] Principal에서 유저 ID를 꺼냅니다.
////        // 닉네임은 DB나 세션에서 가져와야 하지만, 우선 ID를 닉네임처럼 사용하거나 고정합니다.
////        String userId = principal.getName();
////        String nickname = "용띠개발자"; // 추후 UserRepository 연동 필요
////
////        // 서비스 호출 (DTO 전환 및 MongoDB 저장)
////        ChatMessageResponse response = chatService.processMessage(request, userId, nickname);
////
////        // 브로드캐스팅
////        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
////    }
//}