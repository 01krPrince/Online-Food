package com.onlinefood.subscription_service.repository;

import com.onlinefood.subscription_service.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionRepository
        extends MongoRepository<Subscription, String> {

    List<Subscription> findByUserId(String userId);

    List<Subscription> findByProviderId(String providerId);
}
