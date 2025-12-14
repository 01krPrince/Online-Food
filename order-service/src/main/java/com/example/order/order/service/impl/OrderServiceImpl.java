package com.example.order.order.service.impl;

import com.example.order.order.dto.*;
import com.example.order.order.model.Order;
import com.example.order.order.repository.OrderRepository;
import com.example.order.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto) {

        // ✅ Verify User
        UserResponseDto user = webClient.get()
                .uri("http://localhost:8081/users/" + dto.getUserId())
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block();

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // ✅ Get Product
        ProductResponseDto product = webClient.get()
                .uri("http://localhost:8082/products/" + dto.getProductId())
                .retrieve()
                .bodyToMono(ProductResponseDto.class)
                .block();

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // ✅ Calculate price
        double totalPrice = product.getPrice() * dto.getQuantity();

        Order order = new Order(
                null,
                dto.getUserId(),
                dto.getProductId(),
                dto.getQuantity(),
                totalPrice
        );

        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDto(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalPrice()
        );
    }

    @Override
    public List<GetAllOrderResponseDto> fetchAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new GetAllOrderResponseDto(
                        order.getId(),
                        order.getUserId(),
                        order.getProductId(),
                        order.getQuantity(),
                        order.getTotalPrice()
                ))
                .toList();
    }

}
