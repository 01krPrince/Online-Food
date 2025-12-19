package com.tiffin_provider_service.tiffin_provider_service.repository;

import com.tiffin_provider_service.tiffin_provider_service.model.TiffinProvider;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TiffinProviderRepository
        extends MongoRepository<TiffinProvider, String> {

    Optional<TiffinProvider> findByEmail(String email);

    Optional<TiffinProvider> findByPhone(String phone);
}
