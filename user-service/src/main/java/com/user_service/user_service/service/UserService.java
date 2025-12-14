package com.user_service.user_service.service;


import com.user_service.user_service.dto.RegisterRequestDTO;

public interface UserService {
    String register(RegisterRequestDTO dto);
}
