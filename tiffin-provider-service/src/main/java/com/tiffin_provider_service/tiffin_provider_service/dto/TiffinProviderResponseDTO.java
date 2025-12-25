package com.tiffin_provider_service.tiffin_provider_service.dto;

import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TiffinProviderResponseDTO {
    private String id;
    private String name;
    private ProviderStatus status;
}
