package com.onlinefood.subscription_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderDTO {

    private List<String> menuItemIds;
    private Double totalAmount;
}
