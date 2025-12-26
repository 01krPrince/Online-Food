package com.onlinefood.menu_service.dto;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.enums.MenuType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MenuCreateRequestDTO {

    @NotBlank
    private String name;                // Lunch Tiffin / Dinner Menu

    private String description;

    private String menuImage;

    @NotNull
    private MenuType menuType;          // TIFFIN / CASUAL

    @NotNull
    private MealType mealType;          // BREAKFAST / LUNCH / DINNER

    @NotNull
    private FoodType foodType;          // VEG / NON_VEG / BOTH

    // ---------- TIFFIN ONLY ----------
    private Boolean subscriptionEnabled;   // true for tiffin

    private List<String> activeDays;        // MON, TUE, WED

    private String deliveryTimeSlot;        // 12:30–1:30

    private Double pricePerDay;             // tiffin price

    private Double monthlyPrice;            // optional

    // ---------- COMMON ----------
    private boolean active = true;           // provider toggle
}
