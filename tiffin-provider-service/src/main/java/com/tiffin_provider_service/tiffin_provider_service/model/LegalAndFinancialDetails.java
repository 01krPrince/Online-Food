package com.tiffin_provider_service.tiffin_provider_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LegalAndFinancialDetails {

    // Bank Details
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "IFSC Code is required")
    private String ifscCode;

    private String bankName; // Optional, usually auto-fetched via API in real apps

    // KYC Details
    @NotBlank(message = "PAN number is required")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN format")
    private String panNumber;

    @NotBlank(message = "Aadhar number is required")
    @Pattern(regexp = "^[0-9]{12}$", message = "Invalid Aadhar number")
    private String aadharNumber;

    // Optional KYC Documents (file paths or URLs)
    private String panDocumentUrl; // URL or identifier for uploaded PAN document
    private String aadharDocumentUrl; // URL or identifier for uploaded Aadhar document
}
