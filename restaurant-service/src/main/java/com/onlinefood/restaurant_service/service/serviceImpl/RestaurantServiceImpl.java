package com.onlinefood.restaurant_service.service.serviceImpl;

import com.onlinefood.restaurant_service.dto.RestaurantRequestDTO;
import com.onlinefood.restaurant_service.dto.RestaurantResponseDTO;
import com.onlinefood.restaurant_service.enums.RestaurantStatus;
import com.onlinefood.restaurant_service.exception.RestaurantException;
import com.onlinefood.restaurant_service.model.Restaurant;
import com.onlinefood.restaurant_service.repository.RestaurantRepository;
import com.onlinefood.restaurant_service.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Override
    public RestaurantResponseDTO registerRestaurant(RestaurantRequestDTO dto) {
        Restaurant r = new Restaurant();
        r.setName(dto.getName());
        r.setEmail(dto.getEmail());
        r.setPhone(dto.getPhone());
        r.setAddress(dto.getAddress());
        r.setStatus(RestaurantStatus.PENDING);

        repository.save(r);

        // Create and populate the response DTO
        RestaurantResponseDTO response = new RestaurantResponseDTO();
        response.setId(r.getId());
        response.setName(r.getName());
        response.setStatus(r.getStatus());

        return response;
    }

    @Override
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return repository.findAll().stream().map(r -> {
            RestaurantResponseDTO dto = new RestaurantResponseDTO();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setStatus(r.getStatus());
            return dto;
        }).toList();
    }

    @Override
    public RestaurantResponseDTO getRestaurantById(String id) {
        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new RestaurantException("Restaurant not found"));

        RestaurantResponseDTO dto = new RestaurantResponseDTO();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setStatus(r.getStatus());
        return dto;
    }

    @Override
    public void updateStatus(String id, String status) {
        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new RestaurantException("Restaurant not found"));

        r.setStatus(RestaurantStatus.valueOf(status.toUpperCase()));
        repository.save(r);
    }

    @Override
    public List<RestaurantResponseDTO> getAllByStatus(String status) {
        return repository.findAll().stream()
                .filter(r -> r.getStatus().name().equalsIgnoreCase(status))
                .map(r -> {
                    RestaurantResponseDTO dto = new RestaurantResponseDTO();
                    dto.setId(r.getId());
                    dto.setName(r.getName());
                    dto.setStatus(r.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponseDTO updateRestaurant(String id, RestaurantRequestDTO dto) {

        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new RestaurantException("Restaurant not found"));

        // Update only non-null fields
        if (dto.getName() != null && !dto.getName().isBlank()) {
            r.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            r.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            r.setPhone(dto.getPhone());
        }

        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            r.setAddress(dto.getAddress());
        }

        repository.save(r);

        RestaurantResponseDTO response = new RestaurantResponseDTO();
        response.setId(r.getId());
        response.setName(r.getName());
        response.setStatus(r.getStatus());

        return response;
    }



}
