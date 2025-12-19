package com.tiffin_provider_service.tiffin_provider_service.dto;

import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import lombok.Data;

@Data
public class TiffinProviderResponseDTO {
    private String id;
    private String name;
    private ProviderStatus status;
}
