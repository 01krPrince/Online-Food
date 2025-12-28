package com.onlinefood.subscription_service.controller;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    /**
     * CUSTOMER creates subscription
     */
    @PostMapping
    public ResponseEntity<?> createSubscription(
            @RequestBody SubscriptionRequestDTO dto,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeCustomer(userId, role, internalKey);

        return ResponseEntity.ok(
                subscriptionService.createSubscription(dto, userId)
        );
    }

    /**
     * Pause subscription for a specific date
     */
    @PostMapping("/{id}/pause")
    public ResponseEntity<?> pauseForDate(
            @PathVariable String id,
            @RequestParam LocalDate date,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeCustomer(userId, role, internalKey);

        subscriptionService.pauseSubscriptionForDate(id, date, userId);
        return ResponseEntity.ok("Subscription paused for " + date);
    }

    /**
     * Cancel full subscription
     * (refund handled by another service)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelSubscription(
            @PathVariable String id,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeCustomer(userId, role, internalKey);

        subscriptionService.cancelSubscription(id, userId);
        return ResponseEntity.ok("Subscription cancelled successfully");
    }

    // ================= COMMON =================

    private void authorizeCustomer(
            String userId,
            String role,
            String internalKey
    ) {
        if (internalKey == null || !internalKey.equals(gatewaySecret)) {
            throw new RuntimeException("Direct access forbidden");
        }
        if (userId == null || role == null) {
            throw new RuntimeException("Unauthorized access (gateway only)");
        }
        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only CUSTOMER allowed");
        }
    }
}
