//package com.example.backend.controller;
//
//import com.example.backend.domain.chat.ChatMessage;
//import com.example.backend.repository.ChatMessageRepository;
//import com.example.backend.domain.constant.MessageType;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Controller;
//
//@Deprecated
//@Controller
//@RequiredArgsConstructor // SimpleMessageSendingOperations 주입을 위한 생성자 생성
//public class ChatController {
//
//    // @통신선로 [STEP2]
//    private final SimpMessageSendingOperations messagingTemplate;
//
//    // @Messenger Message forever Save [STEP 3]
//    private final ChatMessageRepository chatMessageRepository;
//
//    /*
//    [Step4]
//    무엇이 더 남았을까?
//    이제 메시지는 실시간으로 전달되고 DB에도 저장됩니다. 하지만 유저가 채팅방에 처음 들어왔을 때, MongoDB에 저장된 과거 내역을 쫙 긁어와서 보여주는 과정이 빠져있습니다.
//
//    과거 내역 조회 API: GET /api/chat/room/{roomId} 요청 시 MongoDB에서 해당 방의 로그를 리스트로 반환하는 기능을 만들어야 합니다.
//
//    최종 통합 테스트: 드디어 모든 인프라(RDBMS, NoSQL, Security, WebSocket)를 한꺼번에 테스트할 시간입니다.
//     */
//
//    // 모든 메시지는 이 통로를 통해 방 번호(roomId)별로 구분되어 전달됩니다.
//    @MessageMapping("/chat/message")
//    public void message(com.example.backend.dto.ChatMessage message) {
//        System.out.println("메시지 수신 성공: " + message.getContent()); // 로그 추가
//        if (MessageType.JOIN.equals(message.getType())) {
//            message.setContent(message.getSenderNickname() + "님이 입장하셨습니다.");
//        }
//
//        // 1. MongoDB에 대화 내용 저장
//        ChatMessage log = ChatMessage.builder()
//                .roomId(message.getRoomId())
//                .senderId(message.getSenderId())
//                .senderNickname(message.getSenderNickname())
//                .content(message.getContent())
//                .type(message.getType())
//                .build();
//        chatMessageRepository.save(log);
//
//        // 2. 실시간 메시지 전달
//        // 목적지: /topic/room/{roomId}
//        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), message);
//    }
//    /*
//    ChatController 를 보면 @MessageMapping 주소가 /chat.sendMessage 와 /chat/message 로 중복되거나 섞여 있음.
//    ============ [ REFACTORING ] ============
//    @MessageMapping("/chat.sendMessage")
//    // 결과를 "/topic/public"을 구독 중인 모든 클라이언트에게 전달
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    // 클라이언트가 "/app/chat.sendMessage"로 메시지를 보내면 호출됨
//    // 클라이언트가 /app/chat/message 로 메시지를 보내면 이 메서드가 실행됩니다.
//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message) {
//        // 입장 메시지 처리 (아까 에러 났던 부분 확인!)
//        if (message.getType().equals(MessageType.JOIN)) {
//            message.setContent(message.getSenderNickname() + "님이 입장하셨습니다.");
//        }
//
//        // /topic/room/{roomId} 를 구독 중인 사람들에게 메시지 전달 (브로드캐스팅)
//        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), message);
//    }
//
//*/
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public com.example.backend.dto.ChatMessage addUser(com.example.backend.dto.ChatMessage chatMessage) {
//        // 유저 입장 로직 (세션 관리 등) 추가 가능
//        chatMessage.setContent(chatMessage.getSenderNickname() + "님이 입장하셨습니다.");
//        return chatMessage;
//    }
//}