package com.onlinefood.subscription_service.controller;

import com.onlinefood.subscription_service.enums.OrderType;
import com.onlinefood.subscription_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // 🟢 CUSTOMER: PLACE ONE-TIME ORDER
    @PostMapping
    public ResponseEntity<?> placeOrder(
            @RequestBody Object request,   // DTO later
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only CUSTOMER can place orders");
        }

        return ResponseEntity.ok(service.placeOneTimeOrder(request, userId));
    }

    // 🟢 CUSTOMER: ORDER HISTORY (TOGGLE)
    @GetMapping("/my")
    public ResponseEntity<?> myOrders(
            @RequestParam OrderType type,
            @RequestHeader("X-USER-ID") String userId) {

        return ResponseEntity.ok(service.getOrdersByUser(userId, type));
    }

    // 🟢 PROVIDER: VIEW ORDERS (TOGGLE)
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<?> providerOrders(
            @PathVariable String providerId,
            @RequestParam OrderType type) {

        return ResponseEntity.ok(service.getOrdersByProvider(providerId, type));
    }
}
