package com.onlinefood.subscription_service.controller;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * CUSTOMER creates subscription
     */
    @PostMapping
    public ResponseEntity<?> createSubscription(
            @RequestBody SubscriptionRequestDTO dto,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Only CUSTOMER allowed");
        }

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
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Only CUSTOMER allowed");
        }

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
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Only CUSTOMER allowed");
        }

        subscriptionService.cancelSubscription(id, userId);
        return ResponseEntity.ok("Subscription cancelled successfully");
    }
}
