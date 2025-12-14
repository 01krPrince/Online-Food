package com.example.order.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double totalPrice;
}
