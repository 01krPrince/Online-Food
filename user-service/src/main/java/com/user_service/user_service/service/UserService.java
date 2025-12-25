package com.user_service.user_service.service;


import com.user_service.user_service.dto.LoginRequestDTO;
import com.user_service.user_service.dto.LoginResponseDTO;
import com.user_service.user_service.dto.RegisterRequestDTO;
import com.user_service.user_service.enums.Role;

public interface UserService {
    String register(RegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

    Object getUserById(String userId);

    Object getAllUsers();

    void updateStatus(String id, String status);

    public void updateRole(String userId, Role role);
}
