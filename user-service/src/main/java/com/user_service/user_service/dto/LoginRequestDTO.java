package com.user_service.user_service.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
