package com.onlinefood.restaurant_service.dto;

import com.onlinefood.restaurant_service.enums.RestaurantStatus;
import lombok.Data;

@Data
public class RestaurantResponseDTO {
    private String id;
    private String name;
    private RestaurantStatus status;
}
