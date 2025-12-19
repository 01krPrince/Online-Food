package com.onlinefood.subscription_service.controller;

import com.onlinefood.subscription_service.dto.SubscriptionRequestDTO;
import com.onlinefood.subscription_service.exception.SubscriptionException;
import com.onlinefood.subscription_service.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody SubscriptionRequestDTO dto,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            throw new SubscriptionException("Only CUSTOMER allowed");
        }
        return ResponseEntity.ok(service.createSubscription(dto, userId));
    }

    @PostMapping("/{id}/generate-order")
    public ResponseEntity<?> generateOrder(
            @PathVariable String id,
            @RequestHeader("X-ROLE") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new SubscriptionException("Only ADMIN can generate orders");
        }
        service.generateOrderForSubscription(id);
        return ResponseEntity.ok("Order generated");
    }
}
