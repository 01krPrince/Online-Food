package com.onlinefood.restaurant_service.model;

import com.onlinefood.restaurant_service.enums.RestaurantStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurants")
@Data
public class Restaurant {

    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String address;

    private RestaurantStatus status; // PENDING, ACTIVE, BLOCKED
}
