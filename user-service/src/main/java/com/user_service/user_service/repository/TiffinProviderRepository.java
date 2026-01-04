package com.user_service.user_service.repository;

import com.user_service.user_service.enums.ProviderStatus;
import com.user_service.user_service.model.TiffinProvider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TiffinProviderRepository extends MongoRepository<TiffinProvider, String> {

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    List<TiffinProvider> findByStatus(ProviderStatus status);

    Optional<TiffinProvider> findByUserId(String userId);
}