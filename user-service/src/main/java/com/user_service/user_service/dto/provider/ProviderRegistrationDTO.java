package com.user_service.user_service.dto.provider;

import com.user_service.user_service.model.Address;
import com.user_service.user_service.model.LegalAndFinancialDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProviderRegistrationDTO {

    // ============ USER LOGIN DETAILS (Jo naya banana hai) ============
    
    @NotBlank(message = "Owner Name is required")
    private String name; // Ye User table me jayega

    @Email(message = "Valid email is required")
    @NotBlank
    private String email; // Ye User table + Provider table dono me jayega

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Password is required for login")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password; // Ye sirf User table me jayega (Encrypted)

    @NotBlank(message = "OTP is required for verification")
    private String otp;

    // ============ KITCHEN BUSINESS DETAILS ============

    @NotBlank(message = "Kitchen Name is required")
    @Size(min = 3, max = 100)
    private String kitchenName; // Ye Provider table me jayega

    @NotNull(message = "Address is required")
    @Valid
    private Address address;

    @Valid
    private LegalAndFinancialDetails legalAndFinancialDetails;
}