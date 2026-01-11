package com.example.backend.common.exception;

public class InvalidRefreshTokenException extends CustomException {
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}

