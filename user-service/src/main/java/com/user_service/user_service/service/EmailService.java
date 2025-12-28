package com.user_service.user_service.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
}