package com.onlinefood.menu_service.model;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "menu_items")
@Data
public class MenuItem {

    @Id
    private String id;

    private String providerId;     // redundancy for fast lookup
    private String menuId;         // parent menu

    private String name;
    private String description;

    private double price;

    private String imageUrl;

    private FoodType foodType;     // VEG / NON_VEG
    private MealType mealType;     // BREAKFAST / LUNCH / DINNER

    private boolean available;     // provider toggle

    private double rating;
    private int ratingCount;

    private List<String> tags;     // spicy, healthy, etc.

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
