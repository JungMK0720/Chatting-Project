package com.example.backend.user.dto;

public record TokenResponse(String accessToken, long expiresInMs, String refreshToken) {}