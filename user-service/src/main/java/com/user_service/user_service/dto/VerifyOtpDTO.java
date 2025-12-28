package com.user_service.user_service.dto;
import lombok.Data;

@Data
public class VerifyOtpDTO {
    private String email;
    private String otp;
}