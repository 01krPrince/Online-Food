package com.example.order.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllOrderResponseDto {

    private String orderId;

    private String userId;
    private String productId;

    private int quantity;

    private double totalPrice;
}
