package com.example.backend.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 핸들링 활성화 << Event Bus 방식
// (메시지 처리/엔드포인트 담당)
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer { // 콜백 인터페이스를 사용.

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket에 접속하는 경로 (예: ws://localhost:8080/ws-chat)
        // 시작 지점
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*"); // CORS 설정
//                .withSockJS(); // 낮은 버전 브라우저 호환성 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Message가 흘러갈 곳.

        // 클라이언트가 메시지를 보낼 때 (Publish) 사용하는 접두사
        registry.setApplicationDestinationPrefixes("/app");

        // 클라이언트가 메시지를 받을 때 (Subscribe) 사용하는 접두사
        registry.enableSimpleBroker("/topic");
    }
}