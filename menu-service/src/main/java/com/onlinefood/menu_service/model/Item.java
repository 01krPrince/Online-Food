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
@Document(collection = "items")
public class Item {

    @Id
    private String id;

    // 🔑 OWNERSHIP
    private String providerId;

    // 🔗 MULTI MENU REFERENCE
    private List<String> menuIds;   // can belong to multiple menus

    // 🍛 ITEM INFO
    private String name;
    private String description;
    private String imageUrl;

    // 💰 PRICE
    private double price;

    // 🍽 CONTEXT
    private FoodType foodType;
    private MealType mealType;
    private OrderType orderType;

    // 📦 TIFFIN
    private boolean includedInTiffin;
    private boolean optionalAddon;

    // ⚙️ STATUS
    private boolean available;

    // 📊 RATING
    private double rating;
    private int ratingCount;

    // 🏷 TAGS
    private List<String> tags;

    // 🕒 META
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
