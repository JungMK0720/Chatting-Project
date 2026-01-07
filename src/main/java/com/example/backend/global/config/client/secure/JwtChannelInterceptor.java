package com.example.backend.global.config.client.secure;

import com.example.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
// import java.nio.file.AccessDeniedException; << 이거 아님
import org.springframework.security.access.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 1. 연결 시도(CONNECT) 시점에만 JWT 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    // 인증 성공 시 유저 정보 세팅
                    String userId = jwtTokenProvider.getUserId(token);
                    accessor.setUser(() -> userId);
                } else {
                    throw new AccessDeniedException("인증되지 않은 유저입니다.");
                }
            }
        }
        return message;
    }
}