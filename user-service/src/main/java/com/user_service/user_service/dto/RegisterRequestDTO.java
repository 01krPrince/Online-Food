package com.user_service.user_service.dto;

import com.user_service.user_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;

    private Role role; // ADMIN / RESTAURANT / CUSTOMER
}
