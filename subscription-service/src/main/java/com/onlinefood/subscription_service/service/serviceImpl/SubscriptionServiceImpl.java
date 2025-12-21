package com.onlinefood.subscription_service.service.serviceImpl;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.enums.OrderStatus;
import com.onlinefood.subscription_service.enums.OrderType;
import com.onlinefood.subscription_service.enums.SubscriptionStatus;
import com.onlinefood.subscription_service.model.Order;
import com.onlinefood.subscription_service.model.Subscription;
import com.onlinefood.subscription_service.repository.OrderRepository;
import com.onlinefood.subscription_service.repository.SubscriptionRepository;
import com.onlinefood.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final OrderRepository orderRepo;

    @Override
    public Subscription createSubscription(
            SubscriptionRequestDTO dto,
            String userId) {

        Subscription sub = Subscription.builder()
                .userId(userId)
                .providerId(dto.getProviderId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .deliveryTimes(dto.getDeliveryTimes())
                .pausedDates(new HashSet<>())
                .status(SubscriptionStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        return subscriptionRepo.save(sub);
    }

    @Override
    public void pauseSubscriptionForDate(
            String subscriptionId,
            LocalDate date,
            String userId) {

        Subscription sub = getOwned(subscriptionId, userId);
        sub.getPausedDates().add(date);
        subscriptionRepo.save(sub);
    }

    @Override
    public void cancelSubscription(
            String subscriptionId,
            String userId) {

        Subscription sub = getOwned(subscriptionId, userId);
        sub.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepo.save(sub);
    }

    /**
     * 🔥 AUTO ORDER CREATION (Scheduler)
     */
    @Override
    public void generateDailyOrders() {

        LocalDate today = LocalDate.now();

        List<Subscription> activeSubs =
                subscriptionRepo.findByStatus(SubscriptionStatus.ACTIVE);

        for (Subscription sub : activeSubs) {

            if (today.isBefore(sub.getStartDate())
                    || today.isAfter(sub.getEndDate())) continue;

            if (sub.getPausedDates().contains(today)) continue;

            for (LocalTime time : sub.getDeliveryTimes()) {

                boolean exists =
                        orderRepo.existsBySubscriptionIdAndDeliveryDateAndDeliveryTime(
                                sub.getId(), today, time);

                if (exists) continue;

                Order order = Order.builder()
                        .userId(sub.getUserId())
                        .providerId(sub.getProviderId())
                        .orderType(OrderType.SUBSCRIPTION)
                        .subscriptionId(sub.getId())
                        .deliveryDate(today)
                        .deliveryTime(time)
                        .status(OrderStatus.CREATED)
                        .menuItemIds(List.of())
                        .totalAmount(0.0)
                        .createdAt(LocalDateTime.now())
                        .build();

                orderRepo.save(order);
            }
        }
    }

    private Subscription getOwned(String id, String userId) {
        Subscription sub = subscriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!sub.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }
        return sub;
    }
}
