package com.tiffin_provider_service.tiffin_provider_service.repository;

import com.tiffin_provider_service.tiffin_provider_service.enums.ProviderStatus;
import com.tiffin_provider_service.tiffin_provider_service.model.TiffinProvider;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TiffinProviderRepository
        extends MongoRepository<TiffinProvider, String> {

    boolean existsByUserId(String userId);

    List<TiffinProvider> findByStatus(ProviderStatus status);
}
