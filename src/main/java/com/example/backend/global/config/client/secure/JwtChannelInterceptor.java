package com.example.backend.global.config.client.secure;

import com.example.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
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

        // 어떤 명령이 들어오는지 로그를 찍어보세요.
        System.out.println("Full Accessor: " + accessor);
        System.out.println("Command: " + accessor.getCommand());

        // 1. 연결 시도(CONNECT) 시점에만 JWT 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // [수정된 디버깅 코드] 모든 네이티브 헤더를 안전하게 출력합니다.
            Object nativeHeaders = accessor.getHeader(NativeMessageHeaderAccessor.NATIVE_HEADERS);
            System.out.println("가공되지 않은 원본 헤더들: " + nativeHeaders);

            String test = accessor.getFirstNativeHeader("Authorization");
            System.out.println("추출 시도 토큰: " + test);

            String token = accessor.getFirstNativeHeader("Authorization");



            // Log
            System.out.println("STOMP Header Token: " + token); // 여기서 null이 뜨면 프레임 작성이 잘못된 것입니다.



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