package com.example.order.order.service;

import com.example.order.order.dto.GetAllOrderResponseDto;
import com.example.order.order.dto.OrderRequestDto;
import com.example.order.order.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);

    List<GetAllOrderResponseDto> fetchAllOrders();
}
