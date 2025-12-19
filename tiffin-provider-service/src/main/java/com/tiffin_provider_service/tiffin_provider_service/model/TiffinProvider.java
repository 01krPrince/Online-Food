package com.tiffin_provider_service.tiffin_provider_service.model;

import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tiffin_providers")
@Data
public class TiffinProvider {

    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String address;

    private ProviderStatus status;
}
