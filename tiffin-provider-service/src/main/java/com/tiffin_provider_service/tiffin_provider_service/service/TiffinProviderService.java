package com.tiffin_provider_service.tiffin_provider_service.service;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;

import java.util.List;

public interface TiffinProviderService {

    TiffinProviderResponseDTO apply(String userId, String role, TiffinProviderRequestDTO dto);

    void updateStatus(String providerId, String role, String status);

    Object getById(String id);

    List<?> getByStatus(String status);
}
