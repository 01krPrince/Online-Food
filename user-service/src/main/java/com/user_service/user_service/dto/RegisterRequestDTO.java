package com.user_service.user_service.dto;

import com.user_service.user_service.enums.PublicRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private PublicRole role; // ONLY CUSTOMER / PROVIDER
}
