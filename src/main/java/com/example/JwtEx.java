package com.example;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseCookie;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtEx {

    public String key = "fdasklfjweaifjawefjweifjaslkfd";
    long expirationTime = 1000 * 60 * 30;

    public String generateAccessToken(String userId, String userEmail) {
        return Jwts.builder()
                .header()
                    .add("typ", "jwt")
                    .add("alg", "HS256")
                .and()
                .subject(userId)
                .claim("email", userEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey(key))
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public SecretKey secretKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }


    public ResponseCookie generateAccessTokenCookie(String accessToken) {
        return ResponseCookie
                .from("accessToken", accessToken)
                .secure(true)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .maxAge(360000)
                .build();
    }
}














