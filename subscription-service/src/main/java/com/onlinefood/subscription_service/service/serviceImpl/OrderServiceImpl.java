package com.onlinefood.subscription_service.service.serviceImpl;

import com.onlinefood.subscription_service.dto.UpdateOrderDTO;
import com.onlinefood.subscription_service.enums.OrderStatus;
import com.onlinefood.subscription_service.model.Order;
import com.onlinefood.subscription_service.repository.OrderRepository;
import com.onlinefood.subscription_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;

    @Override
    public Order updateOrder(
            String orderId,
            UpdateOrderDTO dto,
            String userId) {

        Order order = getOwned(orderId, userId);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order already cancelled");
        }

        order.setMenuItemIds(dto.getMenuItemIds());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(OrderStatus.UPDATED);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepo.save(order);
    }

    @Override
    public void cancelOrder(
            String orderId,
            String userId) {

        Order order = getOwned(orderId, userId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

    private Order getOwned(String id, String userId) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }
        return order;
    }
}
