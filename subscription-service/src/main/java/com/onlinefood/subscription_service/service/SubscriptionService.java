package com.onlinefood.subscription_service.service;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.model.Subscription;

import java.time.LocalDate;

public interface SubscriptionService {

    Subscription createSubscription(
            SubscriptionRequestDTO dto,
            String userId
    );

    void pauseSubscriptionForDate(
            String subscriptionId,
            LocalDate date,
            String userId
    );

    void cancelSubscription(
            String subscriptionId,
            String userId
    );

    /**
     * Called automatically by scheduler daily
     */
    void generateDailyOrders();
}
