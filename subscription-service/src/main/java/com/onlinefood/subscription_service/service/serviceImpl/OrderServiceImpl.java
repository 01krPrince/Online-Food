package com.onlinefood.subscription_service.service.serviceImpl;

import com.onlinefood.subscription_service.enums.OrderStatus;
import com.onlinefood.subscription_service.enums.OrderType;
import com.onlinefood.subscription_service.model.Order;
import com.onlinefood.subscription_service.repository.OrderRepository;
import com.onlinefood.subscription_service.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    // ---------------- CUSTOMER ----------------

    @Override
    public Order placeOneTimeOrder(Object request, String userId) {

        // ⚠️ For now: dummy logic (DTO & pricing next step)

        Order order = new Order();
        order.setUserId(userId);
        order.setProviderId("TEMP_PROVIDER_ID"); // replace later
        order.setOrderType(OrderType.ONE_TIME);
        order.setSubscriptionId(null);

        order.setMenuItemIds(List.of("TEMP_MENU_ID")); // replace later
        order.setDeliveryDate(LocalDate.now());
        order.setTotalAmount(0.0); // price engine next step

        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        return repository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(String userId, OrderType type) {
        return repository.findByUserIdAndOrderType(userId, type);
    }

    // ---------------- PROVIDER ----------------

    @Override
    public List<Order> getOrdersByProvider(String providerId, OrderType type) {
        return repository.findByProviderIdAndOrderType(providerId, type);
    }
}
