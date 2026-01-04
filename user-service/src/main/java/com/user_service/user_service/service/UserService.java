package com.user_service.user_service.service;


import com.user_service.user_service.dto.customer.LoginRequestDTO;
import com.user_service.user_service.dto.customer.LoginResponseDTO;
import com.user_service.user_service.dto.customer.RegisterRequestDTO;
import com.user_service.user_service.dto.VerifyOtpDTO;
import com.user_service.user_service.enums.Role;

public interface UserService {
    String register(RegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

    void sendOtp(String email);

    Object getUserById(String userId);

    Object getAllUsers();

    void updateStatus(String id, String status);

    public void updateRole(String userId, Role role);

    void verifyOtp(VerifyOtpDTO dto);

}
