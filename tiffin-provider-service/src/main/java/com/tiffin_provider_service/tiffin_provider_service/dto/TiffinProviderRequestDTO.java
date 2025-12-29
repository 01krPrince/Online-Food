package com.tiffin_provider_service.tiffin_provider_service.dto;

import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import com.tiffin_provider_service.tiffin_provider_service.model.Address;
import com.tiffin_provider_service.tiffin_provider_service.model.LegalAndFinancialDetails;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class TiffinProviderRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number. It should be a 10-digit number starting with 6-9.")
    private String phone;

    @NotBlank(message = "Address is required")
    @Valid
    private Address address;

    @NotBlank(message = "Kitchen name is required")
    @Size(min = 3, max = 100, message = "Kitchen name must be between 3 and 100 characters.")
    private String kitchenName;

    @Valid
    private LegalAndFinancialDetails legalAndFinancialDetails;

    private ProviderStatus status = ProviderStatus.PENDING;

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setLegalAndFinancialDetails(LegalAndFinancialDetails legalAndFinancialDetails) {
        this.legalAndFinancialDetails = legalAndFinancialDetails;
    }
}
