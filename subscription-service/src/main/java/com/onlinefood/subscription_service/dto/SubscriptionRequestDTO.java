package com.onlinefood.subscription_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequestDTO {

    private String providerId;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * breakfast / lunch / dinner times
     */
    private List<LocalTime> deliveryTimes;
}
