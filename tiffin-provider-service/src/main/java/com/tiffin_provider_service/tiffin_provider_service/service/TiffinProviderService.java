package com.tiffin_provider_service.tiffin_provider_service.service;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;

import java.util.List;

public interface TiffinProviderService {

    TiffinProviderResponseDTO registerProvider(TiffinProviderRequestDTO dto);

    List<TiffinProviderResponseDTO> getAllProviders();

    TiffinProviderResponseDTO getProviderById(String id);

    void updateStatus(String id, String status);

    List<TiffinProviderResponseDTO> getAllByStatus(String status);

    TiffinProviderResponseDTO updateProvider(String id, TiffinProviderRequestDTO dto);
}
