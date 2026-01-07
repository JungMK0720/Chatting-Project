package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.global.security.JwtTokenProvider;
import com.example.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    // 1. 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다. 이제 로그인을 진행해주세요.");
    }

    // 2. 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("accessToken", token));
    }

    // JWT Test용 Login APi
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
//        // 임시 테스트용 로직 (실제로는 DB에서 유저를 찾아야 함)
//        if ("testuser".equals(request.getUsername()) && "password123".equals(request.getPassword())) {
//            String token = jwtTokenProvider.createToken(request.getUsername());
//            return ResponseEntity.ok(Map.of("accessToken", token));
//        }
//        return ResponseEntity.status(401).body("아이디 또는 비밀번호가 틀렸습니다.");
//    }
}