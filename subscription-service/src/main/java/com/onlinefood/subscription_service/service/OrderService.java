package com.onlinefood.subscription_service.service;

import com.onlinefood.subscription_service.enums.OrderType;

import java.util.List;

public interface OrderService {

    Object placeOneTimeOrder(Object request, String userId);

    List<?> getOrdersByUser(String userId, OrderType type);

    List<?> getOrdersByProvider(String providerId, OrderType type);
}
