package com.onlinefood.subscription_service.model;

import com.onlinefood.subscription_service.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Document(collection = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    private String id;

    private String userId;
    private String providerId;

    private LocalDate startDate;
    private LocalDate endDate;

    private SubscriptionStatus status;

    /**
     * Multiple delivery times per day
     * Example: breakfast, lunch, dinner
     */
    private List<LocalTime> deliveryTimes;

    /**
     * Pause on specific dates
     */
    private Set<LocalDate> pausedDates;

    private LocalDateTime createdAt;
}
