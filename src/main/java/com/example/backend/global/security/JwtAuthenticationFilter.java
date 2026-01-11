package com.example.backend.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

// 모든 요청에서 토큰을 검사하는 필터
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException { // 해결: 부모 메서드와 동일한 예외 선언

        String token = resolveToken(request);
        System.out.println("Extracted Token: " + token); // 로그 추가
        if (token != null && jwtTokenProvider.validateToken(token)) {
            System.out.println("Token validation success!"); // 로그 추가
            String userId = jwtTokenProvider.getUserId(token);

            // 테스트용
            // [수정] 빈 리스트 대신 ROLE_USER 권한을 부여합니다.
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))); // 권한 추가

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 원본 코드.. 임시 주석 ...
//            // Authentication 객체 생성 (실제 구현 시에는 UserDetailsService 를 쓰는 것이 좋습니다)
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else
            System.out.println("Token validation failed or Token is null"); // 로그 추가

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}