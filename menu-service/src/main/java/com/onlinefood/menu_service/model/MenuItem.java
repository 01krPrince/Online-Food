package com.onlinefood.menu_service.model;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.enums.OrderType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "menu_items")
public class MenuItem {

    @Id
    private String id;

    // 🔑 RELATION
    private String providerId;     // owner
    private String menuId;         // parent menu

    // 🍛 ITEM INFO
    private String name;           // Dal Fry, Paneer Sabji
    private String description;

    private String imageUrl;

    // 💰 PRICE
    private double price;          // per plate OR per meal

    // 🍽 CONTEXT
    private FoodType foodType;     // VEG / NON_VEG
    private MealType mealType;     // BREAKFAST / LUNCH / DINNER
    private OrderType orderType;   // TIFFIN / CASUAL / BOTH

    // 📦 TIFFIN SPECIFIC
    private boolean includedInTiffin;   // part of daily tiffin
    private boolean optionalAddon;      // extra item (curd, sweet)

    // ⚙️ AVAILABILITY
    private boolean available;

    // 📊 RATING
    private double rating;
    private int ratingCount;

    // 🏷 TAGS
    private List<String> tags;     // spicy, healthy, diabetic, etc.

    // 🕒 META
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
