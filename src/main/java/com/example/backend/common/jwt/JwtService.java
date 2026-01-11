package com.example.backend.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JwtService(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.access-exp-ms}") long accessExpMs,
            @Value("${jwt.refresh-exp-ms}") long refreshExpMs
    ) {
        // Decoders.BASE64.decode()는 그대로 써도 무방함
        this.key = Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(base64Secret));
        this.accessExpMs = accessExpMs;
        this.refreshExpMs = refreshExpMs;
    }

    public String generateAccessToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())      // setId -> id
                .subject(userId)                       // setSubject -> subject
                .issuedAt(Date.from(now))              // setIssuedAt -> issuedAt
                .expiration(Date.from(now.plusMillis(accessExpMs))) // setExpiration -> expiration
                .signWith(key)                         // 알고리즘 명시 안 해도 key 타입 보고 자동 선택 (추천)
                .compact();
    }

    public String generateAccessToken(org.springframework.security.core.userdetails.UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities()) // 권한 정보 추가
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessExpMs)))
                .signWith(key)
                .compact();
    }

    public Date getExpiration(String token) {
        return parse(token).getPayload().getExpiration(); // getBody() -> getPayload()
    }

    public String generateRefreshToken(org.springframework.security.core.userdetails.UserDetails user) {
        return generateRefreshToken(user.getUsername());
    }

    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId)
                .claim("typ", "refresh")               // 커스텀 클레임은 여전히 claim()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(refreshExpMs)))
                .signWith(key)
                .compact();
    }

    // 파싱 로직 (이미 네가 잘 고쳐놓은 부분)
    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public String getUserId(String token) {
        return parse(token).getPayload().getSubject();
    }

    public boolean isRefresh(String token) {
        try {
            return "refresh".equals(parse(token).getPayload().get("typ"));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            // parse 메서드 내부에서 만료(Expired), 서명 불일치(Signature) 등을 모두 체크함
            parse(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            // 서명이 일치하지 않을 때
            return false;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 토큰 유효 시간이 만료되었을 때
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.UnsupportedJwtException e) {
            // 토큰 구조가 잘못되었거나 지원되지 않는 형식일 때
            return false;
        } catch (IllegalArgumentException e) {
            // 토큰이 null이거나 비어있을 때
            return false;
        } catch (Exception e) {
            // 그 외 예상치 못한 모든 예외
            return false;
        }
    }
}