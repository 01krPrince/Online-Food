package com.onlinefood.subscription_service.service;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.model.Subscription;

import java.util.List;

public interface SubscriptionService {

    Subscription createSubscription(SubscriptionRequestDTO dto, String userId);

    Subscription pauseSubscription(String id, String userId);

    Subscription resumeSubscription(String id, String userId);

    void cancelSubscription(String id, String userId);

    List<Subscription> getByProvider(String providerId);

    List<Subscription> getAll();

    List<Subscription> getMySubscriptions(String userId);

    void generateOrderForSubscription(String subscriptionId);
}
