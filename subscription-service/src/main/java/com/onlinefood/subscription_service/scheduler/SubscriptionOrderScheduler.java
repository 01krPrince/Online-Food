package com.onlinefood.subscription_service.scheduler;

import com.onlinefood.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionOrderScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Kolkata")
    public void generateDailyOrders() {

        log.info("Daily subscription order job started");

        subscriptionService.generateDailyOrders();

        log.info("Daily subscription order job finished");
    }
}
