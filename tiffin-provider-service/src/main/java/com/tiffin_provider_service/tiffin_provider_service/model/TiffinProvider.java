package com.tiffin_provider_service.tiffin_provider_service.model;

import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "tiffin_providers")
public class TiffinProvider {

    @Id
    private String id;

    private String userId;

    private String name;
    private String email;
    private String phone;
    private Address address;

    private LegalAndFinancialDetails legalAndFinancialDetails;
    private ProviderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
