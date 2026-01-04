package com.user_service.user_service.dto.provider;

import com.user_service.user_service.enums.ProviderStatus;
import com.user_service.user_service.model.Address;
import com.user_service.user_service.model.LegalAndFinancialDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TiffinProviderRequestDTO {

    @NotBlank(message = "Kitchen name is required")
    @Size(min = 3, max = 100)
    private String kitchenName;

    @NotNull(message = "Address is required")
    @Valid
    private Address address;

    @Valid
    private LegalAndFinancialDetails legalAndFinancialDetails;

    @Email
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String phone;
}