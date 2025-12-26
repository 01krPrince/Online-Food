package com.onlinefood.menu_service.model;

import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.enums.MenuType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "menus")
public class Menu {

    @Id
    private String id;

    // 🔑 OWNER
    private String providerId;        // tiffin provider / kitchen owner

    // 🧾 BASIC INFO
    private String name;              // e.g. "Lunch Tiffin", "Dinner Menu"
    private String description;

    private String menuImage;         // banner image

    // 🍽 MENU TYPE
    private MenuType menuType;        // TIFFIN / CASUAL

    // ⏰ MEAL CONTEXT
    private MealType mealType;        // BREAKFAST / LUNCH / DINNER
    private FoodType foodType;        // VEG / NON_VEG / BOTH

    // 📅 TIFFIN-SPECIFIC
    private boolean subscriptionEnabled;   // true for tiffin plans
    private List<String> activeDays;        // MON,TUE,WED...
    private String deliveryTimeSlot;        // "12:30 PM – 1:30 PM"

    // 💰 PRICING (optional at menu level)
    private Double pricePerDay;       // tiffin daily price
    private Double monthlyPrice;      // optional

    // ⚙️ STATUS
    private boolean active;            // provider enable/disable
    private boolean adminApproved;     // safety check

    // 📊 META
    private double rating;
    private int ratingCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
