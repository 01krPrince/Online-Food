package com.tiffin_provider_service.tiffin_provider_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TiffinProviderRequestDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String phone;

    @NotBlank
    private String address;
}
