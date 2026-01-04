package com.user_service.user_service.dto.provider;

import com.user_service.user_service.enums.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TiffinProviderResponseDTO {
    private String id;
    private String kitchenName;
    private String ownerName;
    private ProviderStatus status;
}