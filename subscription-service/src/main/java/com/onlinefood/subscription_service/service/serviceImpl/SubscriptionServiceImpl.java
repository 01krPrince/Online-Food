package com.onlinefood.subscription_service.service.serviceImpl;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.enums.OrderStatus;
import com.onlinefood.subscription_service.enums.OrderType;
import com.onlinefood.subscription_service.enums.SubscriptionStatus;
import com.onlinefood.subscription_service.exception.SubscriptionException;
import com.onlinefood.subscription_service.model.Order;
import com.onlinefood.subscription_service.model.Subscription;
import com.onlinefood.subscription_service.repository.OrderRepository;
import com.onlinefood.subscription_service.repository.SubscriptionRepository;
import com.onlinefood.subscription_service.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;
    private final OrderRepository orderRepository;

    public SubscriptionServiceImpl(
            SubscriptionRepository repository,
            OrderRepository orderRepository) {

        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Subscription createSubscription(
            SubscriptionRequestDTO dto,
            String userId) {

        Subscription sub = new Subscription();
        sub.setUserId(userId);
        sub.setProviderId(dto.getProviderId());
        sub.setPlanType(dto.getPlanType());
        sub.setStartDate(dto.getStartDate());
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setCreatedAt(LocalDateTime.now());

        switch (dto.getPlanType()) {
            case DAILY -> sub.setEndDate(dto.getStartDate().plusDays(1));
            case WEEKLY -> sub.setEndDate(dto.getStartDate().plusDays(7));
            case MONTHLY -> sub.setEndDate(dto.getStartDate().plusDays(30));
        }

        return repository.save(sub);
    }

    @Override
    public Subscription pauseSubscription(String id, String userId) {
        Subscription sub = getOwned(id, userId);
        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException("Only ACTIVE subscriptions can be paused");
        }
        sub.setStatus(SubscriptionStatus.PAUSED);
        return repository.save(sub);
    }

    @Override
    public Subscription resumeSubscription(String id, String userId) {
        Subscription sub = getOwned(id, userId);
        if (sub.getStatus() != SubscriptionStatus.PAUSED) {
            throw new SubscriptionException("Only PAUSED subscriptions can be resumed");
        }
        sub.setStatus(SubscriptionStatus.ACTIVE);
        return repository.save(sub);
    }

    @Override
    public void cancelSubscription(String id, String userId) {
        Subscription sub = getOwned(id, userId);
        sub.setStatus(SubscriptionStatus.CANCELLED);
        repository.save(sub);
    }

    @Override
    public List<Subscription> getMySubscriptions(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Subscription> getByProvider(String providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public List<Subscription> getAll() {
        return repository.findAll();
    }

    // 🔥 STEP-1 FEATURE
    @Override
    public void generateOrderForSubscription(String subscriptionId) {

        Subscription sub = repository.findById(subscriptionId)
                .orElseThrow(() ->
                        new SubscriptionException("Subscription not found"));

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException("Subscription not active");
        }

        LocalDate today = LocalDate.now();

        boolean exists =
                orderRepository.findBySubscriptionId(subscriptionId)
                        .stream()
                        .anyMatch(o -> o.getDeliveryDate().equals(today));

        if (exists) {
            throw new SubscriptionException("Order already generated for today");
        }

        Order order = new Order();
        order.setUserId(sub.getUserId());
        order.setProviderId(sub.getProviderId());
        order.setOrderType(OrderType.SUBSCRIPTION);
        order.setSubscriptionId(subscriptionId);

        order.setMenuItemIds(List.of());
        order.setTotalAmount(0.0);

        order.setDeliveryDate(today);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    private Subscription getOwned(String id, String userId) {
        Subscription sub = repository.findById(id)
                .orElseThrow(() ->
                        new SubscriptionException("Subscription not found"));
        if (!sub.getUserId().equals(userId)) {
            throw new SubscriptionException("Unauthorized access");
        }
        return sub;
    }
}
