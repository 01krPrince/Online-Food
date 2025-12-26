package com.onlinefood.menu_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuUpdateRequestDTO {

    private String name;

    private String description;

    private String menuImage;

    private Boolean active;

    private List<String> activeDays;

    private String deliveryTimeSlot;

    private Double pricePerDay;

    private Double monthlyPrice;
}
