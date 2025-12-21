package com.onlinefood.menu_service.dto;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class MenuItemCreateRequestDTO {

    @NotBlank(message = "Item name is required")
    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 300)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "1.0", message = "Price must be at least 1")
    private Double price;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotNull(message = "Food type is required")
    private FoodType foodType; // VEG / NON_VEG

    @NotNull(message = "Meal type is required")
    private MealType mealType; // BREAKFAST / LUNCH / DINNER

    private List<@Size(max = 20) String> tags;
}
