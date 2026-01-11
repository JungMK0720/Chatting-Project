package com.example.backend.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// 토큰 생성, 검증, 정보 추출 (핵심 로직)
// Generator + Validator
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    // 1. Secret Key 생성: 최소 32바이트 이상의 비밀키가 필요합니다.
    private final String secretKeyString = "your-very-long-and-secret-key-for-our-hidden-chat-app-12345678";
    private final Key key = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

    private final long tokenValidityInMilliseconds = 1000L * 60 * 60; // 1시간

    // 토큰 생성
    public String createToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .subject(userId) // 최신 버전은 setSubject 대신 subject() 사용
                .issuedAt(now)
                .expiration(validity)
                .signWith(key) // 알고리즘은 키 길이에 따라 자동 선택됨
                .compact();
    }

    // 토큰 검증 (유효성 확인)
    public boolean validateToken(String token) {
        try {
            // 최신 버전은 parserBuilder() -> build() -> parseSignedClaims() 순서
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 유저 ID 추출
    public String getUserId(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
}