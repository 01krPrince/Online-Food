package com.onlinefood.subscription_service.model;

import com.onlinefood.subscription_service.enums.OrderStatus;
import com.onlinefood.subscription_service.enums.OrderType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;

    private String userId;
    private String providerId;

    private OrderType orderType;
    private OrderStatus status;

    /**
     * null for ONE_TIME orders
     */
    private String subscriptionId;

    private LocalDate deliveryDate;
    private LocalTime deliveryTime;

    private List<String> menuItemIds;
    private Double totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
