package com.onlinefood.restaurant_service.repository;

import com.onlinefood.restaurant_service.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    // Custom queries can be added here if necessary
}
