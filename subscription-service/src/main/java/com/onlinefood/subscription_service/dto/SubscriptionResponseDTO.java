package com.onlinefood.subscription_service.dto;

import com.onlinefood.subscription_service.enums.MealType;
import com.onlinefood.subscription_service.enums.PlanType;
import com.onlinefood.subscription_service.enums.SubscriptionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionResponseDTO {

    private String id;
    private String providerId;

    private PlanType planType;
    private MealType mealType;

    private LocalDate startDate;
    private LocalDate endDate;

    private SubscriptionStatus status;
}
