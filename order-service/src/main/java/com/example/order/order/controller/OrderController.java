package com.example.order.order.controller;

import com.example.order.order.dto.GetAllOrderResponseDto;
import com.example.order.order.dto.OrderRequestDto;
import com.example.order.order.dto.OrderResponseDto;
import com.example.order.order.model.Order;
import com.example.order.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDto placeOrder(@RequestBody OrderRequestDto requestDto) {
        return orderService.placeOrder(requestDto);
    }

    @GetMapping
    public List<GetAllOrderResponseDto> fetchAllOrders(){
        return orderService.fetchAllOrders();
    }
}
