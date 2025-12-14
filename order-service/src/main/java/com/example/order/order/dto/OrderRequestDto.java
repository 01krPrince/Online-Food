package com.example.order.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    private String userId;
    private String productId;
    private int quantity;
}
