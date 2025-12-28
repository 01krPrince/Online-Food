package com.onlinefood.menu_service.dto;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.enums.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ItemCreateRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(0)
    private Double price;

    private String imageUrl;

    @NotNull
    private FoodType foodType;

    @NotNull
    private MealType mealType;

    @NotNull
    private OrderType orderType;

    private Boolean includedInTiffin;
    private Boolean optionalAddon;

    private Boolean available = true;

    private List<String> tags;
}
