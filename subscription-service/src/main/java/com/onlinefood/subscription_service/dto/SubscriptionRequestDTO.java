package com.onlinefood.subscription_service.dto;

import com.onlinefood.subscription_service.enums.MealType;
import com.onlinefood.subscription_service.enums.PlanType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionRequestDTO {

    @NotBlank
    private String providerId;

    private PlanType planType;
    private MealType mealType;

    private LocalDate startDate;
}
