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
public class MenuItemCreateRequestDTO {

    @NotBlank
    private String name;                // Dal Fry, Roti, Paneer

    private String description;

    @NotNull
    @Min(0)
    private Double price;               // per plate / per meal

    private String imageUrl;

    @NotNull
    private FoodType foodType;          // VEG / NON_VEG

    @NotNull
    private MealType mealType;          // BREAKFAST / LUNCH / DINNER

    @NotNull
    private OrderType orderType;        // TIFFIN / CASUAL / BOTH

    // ---------- TIFFIN ----------
    private Boolean includedInTiffin;   // daily meal part

    private Boolean optionalAddon;      // curd, sweet, extra roti

    // ---------- COMMON ----------
    private boolean available = true;

    private List<String> tags;          // spicy, healthy, diabetic
}
