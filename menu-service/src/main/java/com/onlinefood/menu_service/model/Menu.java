package com.onlinefood.menu_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "menus")
@Data
public class Menu {

    @Id
    private String id;

    private String providerId;     // owner (TIFFIN PROVIDER)

    private String name;           // Breakfast / Lunch / Dinner
    private String description;

    private boolean active;        // provider can enable/disable menu

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
