package com.onlinefood.subscription_service.controller;

import com.onlinefood.subscription_service.dto.UpdateOrderDTO;
import com.onlinefood.subscription_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    /**
     * CUSTOMER updates today's order
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable String id,
            @RequestBody UpdateOrderDTO dto,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeCustomer(userId, role, internalKey);

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
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeCustomer(userId, role, internalKey);

        orderService.cancelOrder(id, userId);
        return ResponseEntity.ok("Order cancelled successfully");
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
