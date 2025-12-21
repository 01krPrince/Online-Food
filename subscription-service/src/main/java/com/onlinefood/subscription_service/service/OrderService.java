package com.onlinefood.subscription_service.service;

import com.onlinefood.subscription_service.dto.UpdateOrderDTO;
import com.onlinefood.subscription_service.model.Order;

public interface OrderService {

    Order updateOrder(
            String orderId,
            UpdateOrderDTO dto,
            String userId
    );

    void cancelOrder(
            String orderId,
            String userId
    );
}
