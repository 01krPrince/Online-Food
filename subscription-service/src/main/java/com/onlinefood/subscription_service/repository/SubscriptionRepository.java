package com.onlinefood.subscription_service.repository;

import com.onlinefood.subscription_service.enums.SubscriptionStatus;
import com.onlinefood.subscription_service.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionRepository
        extends MongoRepository<Subscription, String> {

    List<Subscription> findByStatus(SubscriptionStatus status);
}
