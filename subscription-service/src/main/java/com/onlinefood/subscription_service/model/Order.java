package com.onlinefood.subscription_service.model;

import com.onlinefood.subscription_service.enums.OrderStatus;
import com.onlinefood.subscription_service.enums.OrderType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {

    @Id
    private String id;

    private String userId;
    private String providerId;

    private OrderType orderType;
    private String subscriptionId; // nullable

    private List<String> menuItemIds;
    private Double totalAmount;

    private LocalDate deliveryDate;
    private OrderStatus status;

    private LocalDateTime createdAt;
}
