package com.onlinefood.subscription_service.controller;

import com.onlinefood.subscription_service.dto.UpdateOrderDTO;
import com.onlinefood.subscription_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * CUSTOMER updates today's order
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable String id,
            @RequestBody UpdateOrderDTO dto,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Only CUSTOMER allowed");
        }

        return ResponseEntity.ok(
                orderService.updateOrder(id, dto, userId)
        );
    }

    /**
     * CUSTOMER cancels order
     * (100% refund handled elsewhere)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable String id,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Only CUSTOMER allowed");
        }

        orderService.cancelOrder(id, userId);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}
