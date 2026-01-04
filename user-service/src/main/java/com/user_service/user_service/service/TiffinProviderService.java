package com.user_service.user_service.service;

import com.user_service.user_service.dto.provider.ProviderRegistrationDTO;
import com.user_service.user_service.dto.provider.TiffinProviderRequestDTO;
import com.user_service.user_service.dto.provider.TiffinProviderResponseDTO;
import com.user_service.user_service.model.TiffinProvider;

import java.util.List;

public interface TiffinProviderService {

    TiffinProviderResponseDTO registerNewProvider(ProviderRegistrationDTO dto);

    TiffinProviderResponseDTO apply(String userId, TiffinProviderRequestDTO dto);

    void updateStatus(String providerId, String status);

    TiffinProvider getById(String id);

    List<TiffinProvider> getByStatus(String status);

    Object getByUserId(String userId);
}