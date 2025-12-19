package com.onlinefood.subscription_service.model;

import com.onlinefood.subscription_service.enums.PlanType;
import com.onlinefood.subscription_service.enums.SubscriptionStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "subscriptions")
@Data
public class Subscription {

    @Id
    private String id;

    private String userId;
    private String providerId;

    private PlanType planType;

    private LocalDate startDate;
    private LocalDate endDate;

    private SubscriptionStatus status;

    private LocalDateTime createdAt;
}
