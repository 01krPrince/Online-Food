package com.onlinefood.subscription_service.repository;

import com.onlinefood.subscription_service.enums.OrderType;
import com.onlinefood.subscription_service.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository
        extends MongoRepository<Order, String> {

    List<Order> findByUserIdAndOrderType(String userId, OrderType orderType);

    List<Order> findByProviderIdAndOrderType(String providerId, OrderType orderType);

    List<Order> findBySubscriptionId(String subscriptionId);
}
