package com.onlinefood.menu_service.dto;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.enums.OrderType;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class MenuItemUpdateRequestDTO {

    private String name;

    private String description;

    @Min(0)
    private Double price;

    private String imageUrl;

    private FoodType foodType;

    private MealType mealType;

    private OrderType orderType;

    private Boolean includedInTiffin;

    private Boolean optionalAddon;

    private Boolean available;

    private List<String> tags;
}
