package com.example.backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 설정 (방화벽 역할)

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 기본 인증 및 CSRF 비활성화 (경고 해결 버전)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // 노란 줄을 해결하는 최신 방식입니다.

                // 2. JWT 사용을 위한 세션 정책 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. 요청별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()   // 로그인, 회원가입 허용
                        .requestMatchers("/ws-stomp/**").permitAll()  // 웹소켓 연결 허용
                        .anyRequest().authenticated()                 // 그 외 모든 요청은 인증 필요
                )

                // 4. JWT 필터 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();

//        http
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/ws-stomp/**") // WebSocket 경로 CSRF 제외
//                        .disable() // 테스트 단계에서는 전체 disable 권장
//                )
//                // 세션을 사용하지 않음 (JWT 방식의 핵심)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/ws-stomp/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                // [핵심] JWT 인증 필터를 다시 끼워넣어야 합니다.
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
    }

    // 비밀번호 암호화(BCrypt) 설정
    // BCrypt는 비밀번호를 안전하게 저장하기 위해 설계된 암호화 해시 함수
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}