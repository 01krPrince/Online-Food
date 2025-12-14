package com.onlinefood.restaurant_service.service;

import com.onlinefood.restaurant_service.dto.RestaurantRequestDTO;
import com.onlinefood.restaurant_service.dto.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantService {

    RestaurantResponseDTO registerRestaurant(RestaurantRequestDTO dto);

    List<RestaurantResponseDTO> getAllRestaurants();

    RestaurantResponseDTO getRestaurantById(String id);

    void updateStatus(String id, String status);

    List<RestaurantResponseDTO> getAllByStatus(String status);

    Object updateRestaurant(String id, RestaurantRequestDTO dto);
}
