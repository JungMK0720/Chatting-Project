package com.example.backend.email.util;

public interface EmailSender {
    void send(String to, String subject, String body);
}