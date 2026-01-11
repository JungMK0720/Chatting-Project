package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.AuthRequest;
import com.example.backend.global.security.JwtTokenProvider;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 1. 회원가입: 비밀번호 암호화가 핵심입니다.
    @Transactional
    public void signup(AuthRequest request) {
        userRepository.findByUsername(request.getUsername())
                .ifPresent(u -> { throw new RuntimeException("이미 존재하는 아이디입니다."); });

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // 암호화!
                .nickname(request.getUsername()) // 초기엔 닉네임을 아이디와 동일하게 설정
                .build();

        userRepository.save(user);
    }

    // 2. 로그인: 암호화된 비번과 입력된 비번을 비교합니다.
    public String login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getUsername());
    }
}