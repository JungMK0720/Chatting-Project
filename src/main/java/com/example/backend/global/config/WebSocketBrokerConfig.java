package com.example.backend.global.config;

import com.example.backend.global.config.client.secure.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor // 인터셉터 주입을 위해 추가
// (브로커 설정 담당)
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    // WebSocket 은 Interceptor 가 메시지를 검문
    // 검문소 역할
    // @통신선로 [STEP1]
    private final JwtChannelInterceptor jwtChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 1. 메시지를 받을 때: /topic(1:N), /queue(1:1)로 시작하는 메시지를 브로커가 처리합니다.
        config.enableSimpleBroker("/topic", "/queue");

        // 2. 메시지를 보낼 때: 서버로 보낼 메시지의 접두사를 /app으로 설정합니다.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 3. 채팅 연결 엔드포인트: 클라이언트는 /ws-stomp로 연결을 시도합니다.
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*"); // 테스트를 위해 모든 도메인 허용
//                .withSockJS(); // for Test 주석 처리
    }

    // [중요] 인터셉터를 등록하여 메시지가 오갈 때마다 검문하게 합니다.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }
}