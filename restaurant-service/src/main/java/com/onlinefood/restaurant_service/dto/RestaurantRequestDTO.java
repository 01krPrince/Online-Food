package com.onlinefood.restaurant_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RestaurantRequestDTO {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;
}
