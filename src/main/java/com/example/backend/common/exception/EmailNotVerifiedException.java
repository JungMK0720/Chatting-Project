package com.example.backend.common.exception;

public class EmailNotVerifiedException extends CustomException {
    public EmailNotVerifiedException() {
        super(ErrorCode.EMAIL_NOT_VERIFIED);
    }
}